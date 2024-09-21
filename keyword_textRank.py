import pandas as pd
import networkx as nx
from sklearn.feature_extraction.text import CountVectorizer
from konlpy.tag import Okt
import numpy as np

# 데이터 로드
file_path = 'c:/Users/pc/Desktop/project/NewKey_genie/newkey_key_last.csv'

# Load the CSV file with appropriate encoding
try:
    data = pd.read_csv(file_path, encoding='utf-8')
except UnicodeDecodeError:
    try:
        data = pd.read_csv(file_path, encoding='cp949')
    except UnicodeDecodeError:
        data = pd.read_csv(file_path, encoding='euc-kr')

# 한국어 텍스트 분석을 위한 KoNLPy의 Okt 객체 초기화
okt = Okt()

# 문서를 구성하는 단어 쌍의 동시 등장 빈도수를 기반으로 그래프 생성
def create_graph(texts, window_size=4):
    graph = nx.Graph()
    for text in texts:
        # 명사 추출
        words = okt.nouns(text)
        # 단어 쌍 생성
        for i in range(len(words)):
            for j in range(i+1, min(i+window_size, len(words))):
                if graph.has_edge(words[i], words[j]):
                    graph[words[i]][words[j]]['weight'] += 1
                else:
                    graph.add_edge(words[i], words[j], weight=1)
    return graph

# TextRank 알고리즘 적용하여 키워드 추출
def extract_keywords(graph, top_n=30):
    scores = nx.pagerank(graph, weight='weight')
    sorted_keywords = sorted(scores.items(), key=lambda x: x[1], reverse=True)
    return [word for word, score in sorted_keywords[:top_n]]

# 모든 텍스트 데이터에서 키워드 추출
all_texts = data['key'].tolist()  # 'key'는 텍스트 데이터가 포함된 컬럼 이름
graph = create_graph(all_texts)
keywords = extract_keywords(graph)

# 키워드를 한 셀에 하나씩 포함하여 CSV로 저장
keywords_df = pd.DataFrame(keywords, columns=['Keyword'])
keywords_df.to_csv('c:/Users/pc/Desktop/project/NewKey_genie/top_keywords_textRank.csv', index=False, encoding='utf-8-sig')

# 결과 확인
print(keywords_df.head())
