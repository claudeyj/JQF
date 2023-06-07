import os, sys
from typing import List
from pathlib import Path
from utils import get_all_classes_from_jar, get_all_classes_from_dir, build_project_cp
from d4j import Defects4J

EVOSUITE_JAR_PATH = Path(os.environ['EVOSUITE_JAR_PATH'])
LOG_ROOT_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'log_gen' / 'evosuite').resolve()
REPORT_ROOT_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'report' / 'evosuite').resolve()
EVO_TEST_DIR = (Path(__file__ + '/../../../..') / 'evosuite_jqf_result' / 'evo_test_dir').resolve()

def build_evosuite_cmd(
    class_name:str, 
    proj_cp_list:List[str],
    log_path:Path,
    evo_test_dir:Path=Path('evosuite-tests'),
    evo_report_dir:Path=Path('evosuite-reports'),
    search_budget:int=60, 
    stopping_condition:str='MaxTime', 
    algorithm:str='SIMPLE_RANDOM_SEARCH',
    strategy:str='generateSimpleRandom',
    no_runtime_dependency:bool=True
) -> str:
    '''
    class_name: the canonical name of the class under test
    project_cp: the classpath of the project under test, seperated by os.pathsep
    '''
    log_path.parent.mkdir(parents=True, exist_ok=True)
    evo_test_dir.mkdir(parents=True, exist_ok=True)
    evo_report_dir.mkdir(parents=True, exist_ok=True)
    
    cmd_list = [
        "java",
        "-jar",
        str(EVOSUITE_JAR_PATH.resolve()),
        "-class",
        class_name,
        "-Dtest_dir=" + str(evo_test_dir.resolve()),
        "-Dreport_dir=" + str(evo_report_dir.resolve()),
        "-Dsearch_budget=" + str(search_budget),
        "-Dstopping_condition=" + stopping_condition,
        "-Dalgorithm=" + algorithm,
        "-" + strategy,
        "-Dno_runtime_dependency=" + str(no_runtime_dependency).lower(),
        "-projectCP",
        build_project_cp(proj_cp_list),
        '> ' + str(log_path) + ' 2>&1'
    ]
    return ' '.join(cmd_list)

def gen_tests_for_SF_subjects(sf_root_dir:Path, sf_subjects:List[str]):
    for sf_subject_id in sf_subjects:
        seq, subject_name = sf_subject_id.split('_')
        sut_jar_path = sf_root_dir / sf_subject_id / f'{subject_name}.jar'
        lib_dir = sf_root_dir / sf_subject_id / 'lib'
        assert sut_jar_path.exists(), f'{sut_jar_path} does not exist'
        dependendant_jars = [str(jar_path) for jar_path in lib_dir.glob('*.jar')]
        
        sut_cp_list = [str(sut_jar_path)] + dependendant_jars
        for class_name in get_all_classes_from_jar(sut_jar_path):
            log_path = LOG_ROOT_DIR / sf_subject_id / f'{class_name}.log'
            report_dir = REPORT_ROOT_DIR / sf_subject_id / f'{class_name}'
            test_dir = EVO_TEST_DIR / sf_subject_id / f'{class_name}'
            cmd = build_evosuite_cmd(class_name, sut_cp_list, log_path, evo_test_dir=test_dir, evo_report_dir=report_dir)
            print('Running command: ' + cmd)
            try:
                os.system(cmd)
            except KeyboardInterrupt:
                sys.exit(0)
        
def gen_tests_for_d4j_subjects(d4j_repo_root_dir:Path, d4j_subjects:List[str]):
    for d4j_subject_name in d4j_subjects:
        # d4j_subject_name: chart_1
        proj_name, id = d4j_subject_name.split('_')
        d4j_repo_dir = d4j_repo_root_dir / d4j_subject_name
        d4j_proj = Defects4J(proj_name, id, 'buggy', d4j_repo_dir)
        d4j_proj.checkout_if_not_exist_or_empty()
        d4j_proj.compile()
        proj_cp = d4j_proj.get_proj_cp() # ":" seperated
        bin_dir = d4j_repo_dir / d4j_proj.get_rela_bin_path()
        for class_name in get_all_classes_from_dir(bin_dir):
            log_path = LOG_ROOT_DIR / d4j_subject_name / f'{class_name}.log'
            report_dir = REPORT_ROOT_DIR / d4j_subject_name / f'{class_name}'
            test_dir = EVO_TEST_DIR / d4j_subject_name / f'{class_name}'
            cmd = build_evosuite_cmd(class_name, proj_cp.split(':'), log_path, evo_test_dir=test_dir, evo_report_dir=report_dir)
            print('Running command: ' + cmd)
            try:
                os.system(cmd)
            except KeyboardInterrupt:
                sys.exit(0)
        
if __name__ == '__main__':
    d4j_repo_root_dir = Path(sys.argv[1])
    d4j_subjects = sys.argv[2].split(',')
    gen_tests_for_d4j_subjects(d4j_repo_root_dir, d4j_subjects)
