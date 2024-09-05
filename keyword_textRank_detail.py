import pandas as pd
import networkx as nx
from konlpy.tag import Okt
from sklearn.feature_extraction.text import TfidfVectorizer
import numpy as np

# CSV 파일 읽기
file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\top_keywords_tfidf_mrc.csv'
data = pd.read_csv(file_path, encoding='utf-8-sig')

# 텍스트 전처리
okt = Okt()

stopwords_file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\stopwords-ko.txt'
with open(stopwords_file_path, 'r', encoding='utf-8') as file:
    korean_stopwords = set(file.read().split())

def preprocess(text):
    words = [word for word in okt.nouns(text) if word not in korean_stopwords and len(word) > 1]
    return words

data['processed'] = data[['title', 'who', 'what', 'keywords']].apply(lambda x: preprocess(' '.join(x.dropna())), axis=1)

# TextRank 그래프 생성 및 키워드 추출
def create_graph(texts, window_size=3):
    graph = nx.Graph()
    for text in texts:
        for i in range(len(text)):
            for j in range(i+1, min(i+window_size, len(text))):
                if graph.has_edge(text[i], text[j]):
                    graph[text[i]][text[j]]['weight'] += 1
                else:
                    graph.add_edge(text[i], text[j], weight=1)
    return graph

def extract_keywords_textrank(graph, top_n=30):
    scores = nx.pagerank(graph, weight='weight')
    sorted_keywords = sorted(scores.items(), key=lambda x: x[1], reverse=True)
    return [word for word, score in sorted_keywords[:top_n]]

# 각 문서에 대해 키워드 추출
data['keywords_textrank'] = data['processed'].apply(lambda x: extract_keywords_textrank(create_graph([x])))

# 결과 저장
result_df = data[['title', 'who', 'what', 'keywords', 'keywords_textrank']]
result_df.to_csv('top_keywords_textrank_tfidf_mrc.csv', index=False, encoding='utf-8-sig')

print(result_df.head())
