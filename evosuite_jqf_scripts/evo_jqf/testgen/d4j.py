import os
from os.path import *
import sys
import subprocess

# extracted from legacy code in https://github.com/claudeyj/d4j_scripts
class Defects4J:
    def __init__(self, project_id:str, bug_id:str, buggy_or_fixed:str, proj_home:str): #project_id: chart, bug_id: 1
        self.project_id = project_id
        self.bug_id = bug_id
        self.buggy_or_fixed = buggy_or_fixed
        assert buggy_or_fixed == 'fixed' or buggy_or_fixed == 'buggy'
        self.proj_home = proj_home
        self.rela_src_dir_path = get_fst_v_src_rela_dir(project_id)
        self.properties = {}

    def checkout(self): # check out to fixed version
        if not isdir(self.proj_home):
            os.makedirs(self.proj_home)
        cmd = "defects4j checkout -p {0} -v {1}{2} -w {3}"\
            .format(get_identifier_from_lower_name(self.project_id), self.bug_id,\
                    'f' if self.buggy_or_fixed == 'fixed' else 'b', self.proj_home)
        self.run_cmd(cmd, at_home=False)
        self.load_properties()
        
    def checkout_if_not_exist_or_empty(self):
        if not isdir(self.proj_home) or len(os.listdir(self.proj_home)) == 0:
            self.checkout()
        else:
            self.load_properties()
            
    def run_cmd(self, cmd, output_dir=None, at_home=True, use_java_8_specially=True, discard_err=True):
        # use_java_8_specially: whether to use java 8 specially. This is set to True by default because the Java version outside is 9, which is not compatible with defects4j
        if use_java_8_specially:
            cmd = "PATH=$JAVA_8_HOME/bin:$PATH " + cmd
        if discard_err:
            cmd += " 2>/dev/null"
        if at_home:
            self.cd_home()
        if output_dir is None:
            output_dir = self.proj_home
        print("[DEFECTS4J]running cmd: " + cmd)
        if sys.version.startswith('3.6'):
            result = subprocess.run(cmd.split(' '), stdout=subprocess.PIPE, check=True).stdout.decode('utf-8')
            print(result)
            return result
        result = subprocess.run(cmd, shell=True, capture_output=True)
        print(result.stdout.decode('utf-8'))
        print(result.stderr.decode('utf-8'))
        result.check_returncode()
        return result.stdout.decode('utf-8')

    def load_properties(self):
        with open(join(self.proj_home, 'defects4j.build.properties')) as f:
            lines = f.readlines()
        for line in lines[1:]:
            if '=' in line:
                line = line.strip()
                key, value = line.split('=')
                self.properties[key] = value
            
    def cd_home(self):
        os.chdir(self.proj_home)
        
    def compile(self):
        cmd = "defects4j compile"
        self.run_cmd(cmd)
        
    def clean(self, reset_git=True):
        if reset_git:
            self.git_reset()
        cmd = "git clean -f -d"
        self.run_cmd(cmd)
    
    def git_reset(self):
        cmd = "git reset --hard"
        self.run_cmd(cmd)
        
    def get_proj_cp(self):
        cmd = f'defects4j export -p cp.compile -w {self.proj_home}'
        return self.run_cmd(cmd).strip()
    
    def get_rela_src_path(self):
        if self.project_id != 'mockito' and self.project_id != 'collections' and self.bug_id == '1':
            return get_fst_v_src_rela_dir(self.project_id)
        elif self.project_id == 'mockito' and self.bug_id == '13':
            return get_fst_v_src_rela_dir(self.project_id)
        elif self.project_id == 'collections' and self.bug_id == '25':
            return get_fst_v_src_rela_dir(self.project_id)
        else:
            return self.get_property('d4j.dir.src.classes')
        
    def get_rela_bin_path(self):
        cmd = "defects4j export -p dir.bin.classes"
        return self.run_cmd(cmd).strip()
        
def get_fst_v_src_rela_dir(proj):
    # return the relative path of source code directory of the first version of the project (13 for mockito, 25 for collections, 1 for others)
    src_info = {}
    src_info['chart'] = 'source'
    src_info['cli'] = 'src/java'
    src_info['closure'] = 'src'
    src_info['codec'] = 'src/java'
    src_info['collections'] = 'src/main/java'
    src_info['compress'] = 'src/main/java'
    src_info['csv'] = 'src/main/java'
    src_info['gson'] = 'gson/src/main/java'
    src_info['jacksoncore'] = 'src/main/java'
    src_info['jacksondatabind'] = 'src/main/java'
    src_info['jacksonxml'] = 'src/main/java'
    src_info['jsoup'] = 'src/main/java'
    src_info['jxpath'] = 'src/java'
    src_info['lang'] = 'src/main/java'
    src_info['math'] = 'src/main/java'
    src_info['mockito'] = 'src'
    src_info['time'] = 'src/main/java'
    
    return src_info[proj]

def get_fst_v_rela_bin_tests_path(proj):
    # bin tests information for 1f
    bin_tests_info = {}
    bin_tests_info['chart'] = 'build-tests'
    bin_tests_info['cli'] = 'target/test-classes'
    bin_tests_info['closure'] = 'build/test'
    bin_tests_info['codec'] = 'target/tests'
    bin_tests_info['collections'] = 'target/tests'
    bin_tests_info['compress'] = 'target/test-classes'
    bin_tests_info['csv'] = 'target/test-classes'
    bin_tests_info['gson'] = 'target/test-classes'
    bin_tests_info['jacksoncore'] = 'target/test-classes'
    bin_tests_info['jacksondatabind'] = 'target/test-classes'
    bin_tests_info['jacksonxml'] = 'target/test-classes'
    bin_tests_info['jsoup'] = 'target/test-classes'
    bin_tests_info['jxpath'] = 'target/test-classes'
    bin_tests_info['lang'] = 'target/tests'
    bin_tests_info['math'] = 'target/test-classes'
    bin_tests_info['mockito'] = 'target/test-classes'
    bin_tests_info['time'] = 'target/test-classes'
    return bin_tests_info[proj]

def get_identifier_from_lower_name(proj_lower_name:str) -> str:
    if proj_lower_name in ['jacksoncore', 'jacksondatabind', 'jacksonxml', 'jxpath']:
        if proj_lower_name == 'jacksoncore':
            return 'JacksonCore'
        elif proj_lower_name == 'jacksondatabind':
            return 'JacksonDatabind'
        elif proj_lower_name == 'jacksonxml':
            return 'JacksonXml'
        elif proj_lower_name == 'jxpath':
            return 'JxPath'
    else:
        return proj_lower_name.capitalize()