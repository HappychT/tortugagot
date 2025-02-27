import os
import json

def update_size_in_file(file_path):
    with open(file_path, 'r') as file:
        data = json.load(file)

    def update_size(obj):
        if isinstance(obj, dict):
            for key, value in obj.items():
                if key == "size" and isinstance(value, list):
                    obj[key] = [0.01 if v == 0 else v for v in value]
                else:
                    update_size(value)
        elif isinstance(obj, list):
            for item in obj:
                update_size(item)

    update_size(data)

    with open(file_path, 'w') as file:
        json.dump(data, file, indent=4)

def update_files_in_directory(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.geo.json'):
                file_path = os.path.join(root, file)
                update_size_in_file(file_path)
                print(f"Updated {file_path}")

current_directory = os.getcwd()
update_files_in_directory(current_directory)