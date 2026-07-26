import os

# Suppose __file__ in server/main.py
file_path = os.path.abspath('server/main.py')
print("file_path:", file_path)
print("dirname(__file__):", os.path.dirname(file_path))
static_dir = os.path.abspath(os.path.join(os.path.dirname(file_path), ".."))
print("static_dir:", static_dir)
print("Files in static_dir:", os.listdir(static_dir))
