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

    # K-V 쌍 생성
    output_data = {}

    # 데이터프레임을 순회하며 K-V 쌍 생성
    for _, row in df.iterrows():
        name = row['name']
        # 정규화된 이름 만들기
        normalized_name = re.sub(r"[ .'À֎’:&%\"-]", "", name)

        if row['type'] == 1:
            output_data[f"wytr.ability.archetype.{normalized_name}"] = name
        else:
            output_data[f"wytr.ability.node.{normalized_name}"] = name

    # 기존 JSON 파일과 비교 및 업데이트
    folder_path = './lang/ability'
    
    # 읽어오기 대상 정규식
    pattern = re.compile(r'^wytr\.ability\.(node|archetype)\.[A-Za-z]+$')
    for file_name in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file_name)

        with open(file_path, 'r', encoding='utf-8') as f:
            file_data = json.load(f)

        # 기존 K-V 쌍을 확인
        existing_keys = {key for key in file_data.keys() if pattern.match(key)}
        new_keys = set(output_data.keys())

        # 새로 추가해야 할 K-V 쌍
        to_add = new_keys - existing_keys

        # 제거해야 할 K-V 쌍
        to_remove = existing_keys - new_keys

        # to_remove 키로 시작하는 모든 하위 키들도 추가로 삭제
        extended_to_remove = set(to_remove)  # 원본 to_remove 복사
        for key in file_data.keys():
            for prefix in to_remove:
                if key.startswith(prefix + "."):  # 점(.)을 붙여 정확한 prefix 확인
                    extended_to_remove.add(key)

        # 파일 업데이트
        for key in to_add:
            file_data[key] = output_data[key]

        for key in extended_to_remove:
            if key in file_data:
                print(f"어빌리티 제거됨: {key}")
                del file_data[key]

        # 파일 덮어쓰기
        with open(file_path, 'w', encoding='utf-8') as f:
            json.dump(file_data, f, ensure_ascii=False, indent=4)

    print("어빌리티 업데이트 완료")
