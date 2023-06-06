import os, sys
from typing import List
from pathlib import Path
from utils import get_all_classes_from_jar, build_project_cp, dump_content_to_file

DRIVER_SIMPLE_NAME = 'CGFDynamicDriverTest'
DRIVER_TEMPLATE_PATH = (Path(__file__ + '/../../../..') / 'examples' / 'src' / 'main' / 'resources' / 'driver' / f'{DRIVER_SIMPLE_NAME}.java').resolve()
assert DRIVER_TEMPLATE_PATH.exists(), f'Driver template {DRIVER_SIMPLE_NAME}.java does not exist at {DRIVER_TEMPLATE_PATH}'
DRIVER_PACKAGE_NAME = 'edu.berkeley.cs.jqf.examples.testgen'
DRIVER_PATH = (Path(__file__ + '/../../../..') / 'examples' / 'src' / 'test' / 'java' / DRIVER_PACKAGE_NAME.replace('.', os.path.sep) / f'{DRIVER_SIMPLE_NAME}.java').resolve()
# assert DRIVER_PATH.exists(), f'CGFDynamicTest.java does not exist at {DRIVER_PATH}'

CUT_TARGET_ROOT_DIR = (Path(__file__ + '/../../../..') / 'examples' / 'cut_target').resolve()
LOG_ROOT_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'log_gen' / 'evo_jqf').resolve()
DEBUG_ROOT_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'debug' / 'evo_jqf').resolve()
EVO_JQF_TEST_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'evo_jqf_test_dir').resolve()
EVO_JQF_TEST_BIN_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'evo_jqf_test_bin_dir').resolve()

def build_jqf_command(
    subject_name:str,
    class_name:str,
    proj_cp_list:List[str],
    log_path:Path
) -> str:
    '''
    class_name: the canonical name of the class under test
    project_cp: the classpath of the project under test, seperated by os.pathsep
    '''
    log_path.parent.mkdir(parents=True, exist_ok=True)
    cmd_list = [
        'mvn clean compile test-compile',
        'jqf:fuzz',
        f'-Dclass={DRIVER_PACKAGE_NAME}.{DRIVER_SIMPLE_NAME}',
        f'-Dmethod={get_fuzz_method_name(class_name)}',
        '-Dtime=1m',
        '-Djqf.ei.MAX_INPUT_SIZE=102400',
        '-Djqf.cov.sut_only=true',
        f'-Djqf.sut_classpath={build_project_cp(proj_cp_list + [str(CUT_TARGET_ROOT_DIR / subject_name)])}',
        f'-Djqf.cut={class_name}',
        '-Djanala.verbose=true',
        f'> {str(log_path)}',
        '2>&1'
    ]

    return ' '.join(cmd_list)

def load_cgf_driver(driver_path:Path) -> str:
    with open(driver_path, 'r') as f:
        return f.read()
    
def insert_at_end(content:str, insert_content:str) -> str:
    content = content.strip()
    # make sure the last line is "}"
    lines = content.split('\n')
    assert lines[-1].strip() == '}'
    lines[-1] = insert_content + '\n' + lines[-1]
    
    return '\n'.join(lines)

def build_fuzz_test_case(subject_name:str, sut_name:str, proj_cp_list:List[str], target_root_dir:Path, debug_root_dir:Path) -> str:
    return build_fuzz_head(sut_name, proj_cp_list) \
        + build_fuzz_body(sut_name, proj_cp_list, target_root_dir / subject_name, debug_root_dir / subject_name) \
        + build_fuzz_tail()

def build_test_gen_config(sut_name:str, proj_cp_list:List[str]) -> str:
    return f'@TestGenerationConfiguration(targetClassCanonicalName = "{sut_name}",' + '\n' \
        + f'projectClassPath = {build_project_cp(proj_cp_list)}'
    
def build_fuzz_head(sut_name:str, proj_cp_list:List[str]) -> str:
    head = ''
    head += '@Fuzz\n'
    fuzz_method_name = get_fuzz_method_name(sut_name)
    test_gen_config = build_test_gen_config(sut_name, proj_cp_list)
    head += ('public void ' + fuzz_method_name \
        + f'(@From (CGFTestSuiteGenerator.class) {test_gen_config}) String testSuiteContent)' + ' {\n')
    head += 'try {\n'
    
    return head

def build_fuzz_body(sut_name:str, proj_cp_list:List[str], target_root_dir:Path, debug_root_dir:Path) -> str:
    body = ''
    body += 'assumeTrue(testSuiteContent != null);\n'
    body += f'String targetRootDir = "{target_root_dir}";\n'
    body += f'String debugRootDir = "{debug_root_dir}";\n'
    body += f'TestEvaluator evaluator = new TestEvaluator({build_project_cp(proj_cp_list)}, targetRootDir, debugRootDir);\n'
    body += f'assumeTrue(evaluator.compile("{sut_name}Test", testSuiteContent));\n'
    body += f'evaluator.execute("{sut_name}Test");\n'
    return body

def build_fuzz_tail() -> str:
    tail = ''
    tail += '} catch (Exception e) {\n'
    tail += 'e.printStackTrace();\n'
    tail += '}\n'
    tail += '}\n'
    return tail

def append_line(content:str, line:str) -> str:
    return content + '\n' + line

def build_driver_content(subject_name:str, sut_name_list:List[str], proj_cp_list:List[str], target_root_dir:Path, debug_root_dir:Path) -> str:
    driver_content = load_cgf_driver(DRIVER_TEMPLATE_PATH)
    for sut_name in sut_name_list:
        fuzz_test_case = build_fuzz_test_case(subject_name, sut_name, proj_cp_list, target_root_dir, debug_root_dir)
        driver_content = insert_at_end(driver_content, fuzz_test_case)
    return driver_content

def gen_tests_for_SF_subjects(sf_root_dir:Path, sf_subjects:List[str]):
    for sf_subject_id in sf_subjects:
        seq, subject_name = sf_subject_id.split('_')
        sut_jar_path = sf_root_dir / sf_subject_id / f'{subject_name}.jar'
        lib_dir = sf_root_dir / sf_subject_id / 'lib'
        assert sut_jar_path.exists(), f'{sut_jar_path} does not exist'
        dependendant_jars = [str(jar_path) for jar_path in lib_dir.glob('*.jar')]
        
        sut_cp = os.pathsep.join([str(sut_jar_path)] + dependendant_jars)
        for class_name in get_all_classes_from_jar(sut_jar_path):
            log_path = LOG_ROOT_DIR / sf_subject_id / f'{class_name}.log'
            debug_dir = DEBUG_ROOT_DIR / sf_subject_id / f'{class_name}'
            test_dir = EVO_JQF_TEST_BIN_DIR / sf_subject_id / f'{class_name}'

            # get driver test content
            driver_content = build_driver_content(subject_name, [class_name], [sut_cp], test_dir, debug_dir)
            # # dump driver test content to file
            dump_content_to_file(driver_content, DRIVER_PATH)
            
            # run evo-jqf
            cmd = build_jqf_command(subject_name, class_name, [sut_cp], log_path)
            print('Running command: \n' + cmd)
            try:
                os.system(cmd)
            except KeyboardInterrupt:
                sys.exit(1)
                
# utility functions
def get_fuzz_method_name(sut_name:str) -> str:
    sut_under_score = sut_name.replace('.', '_')
    return 'testWithGenerator_' + sut_under_score

if __name__ == '__main__':
    # proj_cp_list = ['SF110-20130704/2_a4j/a4j.jar', 'SF110-20130704/2_a4j/lib/jox116.jar', 'SF110-20130704/2_a4j/lib/log4j-1.2.4.jar']
    # fuzz_test_case = build_fuzz_test_case('2_a4j', 'net.kencochrane.a4j.beans.BlendedSearch', proj_cp_list, 'cut_target', 'debug')
    
    # print(fuzz_test_case)
    # driver_content = build_driver_content('2_a4j', ['net.kencochrane.a4j.beans.BlendedSearch'], proj_cp_list, 'cut_target', 'debug')
    # print(driver_content)
    
    gen_tests_for_SF_subjects(Path('/home/jun/research/test_gen/JQF/examples/src/main/resources/SF110-20130704'), ['2_a4j'])