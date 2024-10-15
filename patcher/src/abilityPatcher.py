import requests
import pandas as pd
import re
import json
from bs4 import BeautifulSoup
import os

def run_update():
    # 다섯 가지 직업 항목
    classes = ['archer', 'warrior', 'shaman', 'assassin', 'mage']

    # 데이터를 저장할 리스트
    data_list = []

    # API 호출 및 응답 처리
    for class_name in classes:
        url = f'https://api.wynncraft.com/v3/ability/tree/{class_name}'
        response = requests.get(url)

        if response.status_code == 200:
            data = response.json()

            # 'archetypes' 항목 처리
            if 'archetypes' in data:
                for key, value in data['archetypes'].items():
                    name_html = value['name']
                    # HTML 태그 제거
                    name = BeautifulSoup(name_html, 'html.parser').get_text()
                    data_list.append({'name': name, 'type': 1})

            # 'pages' 항목 처리
            if 'pages' in data:
                for page_key, page_value in data['pages'].items():
                    for ability_key, ability_value in page_value.items():
                        name_html = ability_value['name']
                        # HTML 태그 제거
                        name = BeautifulSoup(name_html, 'html.parser').get_text()
                        data_list.append({'name': name, 'type': 0})
        else:
            print(f"Failed to get data for {class_name}")

    # 데이터프레임으로 변환
    df = pd.DataFrame(data_list)

    # 데이터프레임 확인
    print(df.head())

    # K-V 쌍 생성
    output_data = {}

    # 데이터프레임을 순회하며 K-V 쌍 생성
    for _, row in df.iterrows():
        name = row['name']
        # 정규화된 이름 만들기
        normalized_name = re.sub(r"[ .'À֎’:&%\"-]", "", name)

        if row['type'] == 1:
            output_data[f"wytr.ability.arch.{normalized_name}"] = name
        else:
            output_data[f"wytr.ability.node.{normalized_name}"] = name

    # 출력 준비된 데이터 확인
    print(json.dumps(output_data, indent=4))

    # 기존 JSON 파일과 비교 및 업데이트
    folder_path = './lang/ability'
    for file_name in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file_name)

        with open(file_path, 'r', encoding='utf-8') as f:
            file_data = json.load(f)

        # 기존 K-V 쌍을 확인
        existing_keys = set(file_data.keys())
        new_keys = set(output_data.keys())

        # 새로 추가해야 할 K-V 쌍
        to_add = new_keys - existing_keys

        # 제거해야 할 K-V 쌍
        to_remove = existing_keys - new_keys

        # 파일 업데이트
        for key in to_add:
            file_data[key] = output_data[key]

        for key in to_remove:
            del file_data[key]

        # 파일 덮어쓰기
        with open(file_path, 'w', encoding='utf-8') as f:
            json.dump(file_data, f, ensure_ascii=False, indent=4)

    print("파일 업데이트 완료")
