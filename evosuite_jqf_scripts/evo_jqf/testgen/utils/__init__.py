from pathlib import Path
from typing import List
from zipfile import ZipFile
import os

def get_all_classes_from_jar(jar_path: Path) -> List[str]:
    """
    Returns a list of all classes in a jar file
    """
    classes : List[str] = []
    with ZipFile(jar_path.resolve(), 'r') as jar_file:
        for file_name in jar_file.namelist():
            # exclude directories and inner classes
            if file_name.endswith('.class') and '$' not in file_name:
                classes.append(file_name[:-6].replace('/', '.'))
    
    return classes

def get_all_classes_from_dir(dir_path: Path) -> List[str]:
    """
    Returns a list of all classes in a directory
    """
    classes : List[str] = []
    for file_path in dir_path.rglob('*.class'):
        # exclude inner classes
        if '$' not in file_path.name:
            classes.append(str(file_path.relative_to(dir_path))[:-6].replace('/', '.'))
    
    return classes

def java_file_to_content(java_file: Path) -> str:
    with open(java_file, 'r') as f:
        return f.read()
    
def dump_content_to_file(content: str, file_path: Path) -> None:
    with open(file_path, 'w') as f:
        f.write(content)
    
def build_project_cp(proj_cp_list:List[str]) -> str:
    return f'"{os.path.pathsep.join(proj_cp_list)}"'