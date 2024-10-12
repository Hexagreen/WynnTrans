import requests
import pandas as pd
import json
import re
import os

# 데이터프레임 초기화
df = pd.DataFrame(columns=["name", "type", "lore"])

# type을 정수로 매핑
type_mapping = {
    "ingredient": 1,
    "material": 2,
    "armour": 3,
    "weapon": 4,
    "accessory": 5,
    "tome": 6,
    "charm": 7,
    "tool": 8,
}

# API에서 데이터 가져오는 함수
def fetch_api_data():
    api_url = "https://api.wynncraft.com/v3/item/database?fullResult"
    response = requests.get(api_url)
    if response.status_code == 200:
        return response.json()
    else:
        return None

# 데이터프레임 생성 함수
def create_dataframe(api_response):
    data = []
    
    for key, value in api_response.items():
        name = key
        type_value = value.get("type")
        lore = value.get("lore", None)  # lore가 없으면 null로 처리

        if type_value == "material":
            # 이름에서 숫자 제거
            name = re.sub(r" \d$", "", name)

        # 중복된 항목은 추가하지 않음
        if not any(df['name'] == name):
            type_int = type_mapping.get(type_value, 0)  # 매핑되지 않으면 0으로 처리
            data.append([name, type_int, lore])

    return pd.DataFrame(data, columns=["name", "type", "lore"])

# 데이터프레임 갱신 함수
def update_dataframe(new_df):
    global df
    
    # 기존 데이터와 비교
    removed_items = df[~df['name'].isin(new_df['name'])]  # 기존에 있었는데 삭제된 항목
    added_items = new_df[~new_df['name'].isin(df['name'])]  # 새로 추가된 항목

    # 기존 데이터프레임 덮어쓰기
    df = new_df

    return added_items, removed_items

def task_T(added_items):
    json_result = {}
    
    for _, row in added_items.iterrows():
        name = row['name']
        type_int = row['type']
        lore = row['lore']
        
        # type을 문자열로 변환
        strType = {v: k for k, v in type_mapping.items()}.get(type_int)
        keyName = re.sub(r"[ .'À֎’:&%\"-]", "", name)
        
        # JSON 생성
        json_result[f"wytr.item.{strType}.{keyName}"] = name
        if lore:
            json_result[f"wytr.item.{strType}.{keyName}.lore"] = lore
    
    return json_result
import os

# 작업 R 함수
def task_R(added_items_json, removed_items):
    old_folder = "./lang"  # old 폴더 경로
    json_files = [f for f in os.listdir(old_folder) if f.endswith('.json')]

    removed_items_json = {}
    
    # 삭제된 항목을 작업 T와 같은 형식으로 변환
    for _, row in removed_items.iterrows():
        name = row['name']
        type_int = row['type']
        strType = {v: k for k, v in type_mapping.items()}.get(type_int)
        keyName = re.sub(r"[ .'À֎’:&%\"-]", "", name)
        
        removed_items_json[f"wytr.item.{strType}.{keyName}"] = name
        if row['lore']:
            removed_items_json[f"wytr.item.{strType}.{keyName}.lore"] = row['lore']
    
    # 각 JSON 파일을 수정
    for json_file in json_files:
        json_path = os.path.join(old_folder, json_file)
        
        # 기존 파일 읽기
        with open(json_path, 'r') as f:
            file_data = json.load(f)
        
        # 삭제할 항목 제거
        for key in removed_items_json:
            if key in file_data:
                del file_data[key]
        
        # 새로 추가할 항목 추가
        for key, value in added_items_json.items():
            file_data[key] = value
        
        # 수정된 파일 저장
        with open(json_path, 'w') as f:
            json.dump(file_data, f, indent=4)

# 파일 경로 설정
dataframe_file_path = './dataframe.csv'

# 데이터프레임을 CSV로 저장하는 함수
def save_dataframe(df):
    df.to_csv(dataframe_file_path, index=False)
    print(f"데이터프레임이 '{dataframe_file_path}'에 저장되었습니다.")

# 기존 CSV에서 데이터프레임을 불러오는 함수
def load_dataframe():
    if os.path.exists(dataframe_file_path):
        return pd.read_csv(dataframe_file_path)
    else:
        return pd.DataFrame(columns=["name", "type", "lore"])

def run_update():
    global df
    
    # 1. 기존에 저장된 데이터프레임 불러오기
    df = load_dataframe()

    # 2. API 응답 받아오기
    api_data = fetch_api_data()
    
    if not api_data:
        print("API 데이터를 가져오는 데 실패했습니다.")
        return

    # 3. 새로운 응답을 데이터프레임으로 변환
    new_df = create_dataframe(api_data)
    
    # 4. 기존 데이터프레임과 비교
    added_items, removed_items = update_dataframe(new_df)
    
    # 5. 새로운 데이터프레임을 CSV로 저장
    save_dataframe(new_df)
    
    # 6. 작업 T 호출
    added_items_json = task_T(added_items)
    
    # 7. 작업 R 호출
    task_R(added_items_json, removed_items)
    
    print("갱신 작업이 완료되었습니다.")