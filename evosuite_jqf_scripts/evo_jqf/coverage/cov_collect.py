import os, sys
from pathlib import Path
from typing import List
sys.path.append(str(Path(__file__).parent.parent))
from testgen.utils import get_all_classes_from_jar

EVOSUITE_JAR_PATH = Path(os.environ['EVOSUITE_JAR_PATH'])
JUNIT_JAR_PATH = Path(os.environ['JUNIT_JAR_PATH'])

EVOSUITE_LOG_ROOT_DIR = Path(__file__ + '../../../../..').resolve() / 'evosuite_jqf_result' / 'log_cov' / 'evosuite'
EVO_TEST_SRC_ROOT_DIR = Path(__file__ + '../../../../..').resolve() / 'evosuite_jqf_result' / 'evo_test_dir'
EVO_TEST_BIN_ROOT_DIR = Path(__file__ + '../../../../..').resolve() / 'evosuite_jqf_result' / 'evo_test_bin_dir'

EVO_JQF_LOG_ROOT_DIR = Path(__file__ + '../../../../..').resolve() / 'evosuite_jqf_result' / 'log_cov' / 'evo_jqf_len_20'
EVO_JQF_TEST_DIR = Path(__file__ + '../../../../..').resolve() / 'examples' / 'debug'

def build_cov_cmd(
    class_name:str,
    project_cp:str,
    log_path:Path,
    evo_test_bin_dir:Path,
) -> str:
    '''
    class_name: the canonical name of the class under test
    project_cp: the classpath of the project under test, seperated by os.pathsep
    '''
    log_path.parent.mkdir(parents=True, exist_ok=True)
    option = "-measureCoverage"
    
    cmd_list = [
        "java",
        "-jar",
        str(EVOSUITE_JAR_PATH.resolve()),
        option,
        "-class",
        class_name,
        "-Djunit=" + str(evo_test_bin_dir.resolve()),
        "-projectCP",
        project_cp + ":" + str(evo_test_bin_dir.resolve()),
        "-Dhandle_static_fields=false", # to avoid the error of executing in the unsafe thread
        "> " + str(log_path) + " 2>&1"
    ]
    return ' '.join(cmd_list)
    
# only required for evosuite
def build_compile_cmd(
    class_name:str,
    project_cp:str,
    evo_test_bin_dir:Path,
    evo_test_src_dir:Path=Path('evosuite-tests'),
) -> str:
    '''
    class_name: the canonical name of the class under test
    project_cp: the classpath of the project under test, seperated by os.pathsep
    evo_test_bin_dir: the directory to store the compiled test classes
    evo_test_src_dir: the directory to store the source test classes
    '''
    cmd_list = [
        "javac",
        "-cp",
        "{}:{}".format(str(JUNIT_JAR_PATH.resolve()), project_cp),
        "-d",
        "{}".format(str(evo_test_bin_dir.resolve())),
        "{}".format(str(evo_test_src_dir.resolve() / Path(class_name.replace('.', '/') + '_ESTest.java')))
    ]
    
    return ' '.join(cmd_list)

def collect_coverage_for_SF_subjects(sf_root_dir:Path, sf_subjects:List[str]) -> None:
    for sf_subject_id in sf_subjects:
        seq, subject_name = sf_subject_id.split('_')
        sut_jar_path = sf_root_dir / Path(sf_subject_id) / Path(f'{subject_name}.jar')
        lib_dir = sf_root_dir / Path(sf_subject_id) / Path('lib')
        assert sut_jar_path.exists(), f'{sut_jar_path} does not exist'
        dependendant_jars = [str(jar_path) for jar_path in lib_dir.glob('*.jar')]
        
        sut_cp = os.pathsep.join([str(sut_jar_path)] + dependendant_jars)
        try:
            for class_name in get_all_classes_from_jar(sut_jar_path):
                if tool == 'evosuite':
                    log_path = EVOSUITE_LOG_ROOT_DIR / Path(sf_subject_id) / Path(f'{class_name}.log')
                    evo_test_src_dir = EVO_TEST_SRC_ROOT_DIR / Path(sf_subject_id) / Path(f'{class_name}')
                    assert evo_test_src_dir.exists(), f'{evo_test_src_dir} does not exist'
                    evo_test_bin_dir = EVO_TEST_BIN_ROOT_DIR / Path(sf_subject_id) / Path(f'{class_name}')
                    evo_test_bin_dir.mkdir(parents=True, exist_ok=True)
                    compile_cmd = build_compile_cmd(class_name, sut_cp, evo_test_bin_dir=evo_test_bin_dir, evo_test_src_dir=evo_test_src_dir)
                    print("Compiling with command: " + compile_cmd)
                    os.system(compile_cmd)
                    cov_cmd = build_cov_cmd(class_name, sut_cp, log_path, evo_test_bin_dir)
                    print("Collecting coverage with command: " + cov_cmd)
                    os.system(cov_cmd)
                elif tool == 'evo_jqf':
                    evo_test_src_dir = EVO_JQF_TEST_DIR / Path(sf_subject_id) / Path('src')
                    evo_test_bin_dir = EVO_JQF_TEST_DIR / Path(sf_subject_id) / Path('bin')
                    assert evo_test_src_dir.exists() and evo_test_bin_dir.exists(), f'{evo_test_src_dir} or {evo_test_bin_dir} does not exist'
                    test_cnt = len([name for name in os.listdir(evo_test_src_dir) if name.startswith(class_name + 'Test_')])
                    if test_cnt == 0:
                        continue
                    assert test_cnt > 0 and test_cnt == len([name for name in os.listdir(evo_test_bin_dir) if name.startswith(class_name + 'Test_')]), f'{evo_test_src_dir} and {evo_test_bin_dir} inconsistent'
                    for i in range(test_cnt):
                        log_path = EVO_JQF_LOG_ROOT_DIR / Path(sf_subject_id) / Path(f'{class_name}_{i}.log')
                        evo_test_src_cnt_dir = evo_test_src_dir / Path(f'{class_name}Test_{i}')
                        evo_test_bin_cnt_dir = evo_test_bin_dir / Path(f'{class_name}Test_{i}')
                        assert evo_test_src_cnt_dir.exists() and evo_test_bin_cnt_dir.exists(), f'{evo_test_src_cnt_dir} or {evo_test_bin_cnt_dir} does not exist'
                        cov_cmd = build_cov_cmd(class_name, sut_cp, log_path, evo_test_bin_cnt_dir)
                    
                        print("Collecting coverage with command: " + cov_cmd)
                        os.system(cov_cmd)
        except KeyboardInterrupt:
            sys.exit(0)
            
            
if __name__ == '__main__':
    tool = sys.argv[1]
    assert tool in ['evosuite', 'evo_jqf'], f'Unknown tool: {tool}'
    sf_root_dir = Path(sys.argv[2])
    sf_subjects = sys.argv[3:]
    collect_coverage_for_SF_subjects(sf_root_dir, sf_subjects)