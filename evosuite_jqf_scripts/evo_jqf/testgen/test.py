from pathlib import Path
utils_path = Path('/home/jun/research/test_gen/JQF/evosuite_jqf_scripts/evo_jqf/testgen/utils')
init_path = utils_path / 'tmp' / '__init__.py'
print(Path('evo_jqf/testgen/test.py' + '/..').resolve())
print(init_path)
print(init_path.resolve())
print(type(init_path))
print(type(init_path.resolve()))