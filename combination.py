import pandas as pd
import numpy as np
import re
from gensim import corpora
from gensim.models.ldamodel import LdaModel
from sklearn.feature_extraction.text import TfidfVectorizer
from pytrends.request import TrendReq

# 데이터 로드 (CSV 파일)
final = pd.read_csv('news_data_19961231.csv', encoding='cp949')

# 한글 단어만 추출하는 함수
def extract_korean_words(text):
    return re.findall(r'[가-힣]+', text)

# 텍스트 데이터에서 한글 단어만 추출
sentences = [extract_korean_words(word) for sentence in final['key'] for word in sentence.split(',')]

# 문서-단어 행렬 생성 및 LDA 모델 학습
dictionary = corpora.Dictionary(sentences)
corpus = [dictionary.doc2bow(text) for text in sentences]
# LDA 모델의 파라미터 조정: 토픽의 수를 조절하고, 더 많은 패스와 반복을 통해 모델의 정확도를 높입니다.
lda_model = LdaModel(corpus, num_topics=10, id2word=dictionary, passes=15, iterations=400)

# LDA에서 추출한 주요 단어들
words_lda = []
for idx, topic in lda_model.print_topics(-1):
    words_lda.extend([word for word in topic.split('"')[1::2]])

# TF-IDF 계산 및 가중치 계산
stop_words = ['대해', '있는', '했다', '합니다', '하는', '하고', '있다', '된다', '됩니다']  # 불용어 목록 확장
tfidf = TfidfVectorizer(stop_words=stop_words)
res = tfidf.fit_transform([' '.join(sentence) for sentence in sentences])

# res 배열의 모양 확인 및 importance 계산
print("res 배열의 모양:", res.shape)
importance = np.sum(res, axis=0)

# TF-IDF 특성 이름 확인
tfidf_feature_names = np.array(tfidf.get_feature_names_out())
print("TF-IDF 특성 이름:", tfidf_feature_names)

# word_weights 계산
word_weights = {}
for word in words_lda:
    if len(word) > 1 and word in tfidf_feature_names:
        idx = np.where(tfidf_feature_names == word)[0][0]
        word_weights[word] = importance[0, idx]

# 상위 30개 키워드 추출
top_30_keywords = sorted(word_weights.items(), key=lambda x: x[1], reverse=True)[:30]
top_30_keywords = [word for word, weight in top_30_keywords]

# Google Trends API 연결 및 상위 20개 키워드 추출
pytrends = TrendReq(hl='ko-KR', tz=540)
df_trends = pytrends.trending_searches(pn='south_korea')
google_trends_top_20 = df_trends[0].tolist()

# 상위 30개 키워드와 상위 20개 트렌드 키워드 사이의 공통 키워드 추출
common_keywords = list(set(top_30_keywords).intersection(set(google_trends_top_20)))

# 공통된 상위 10개 키워드 출력
print("CSV 파일과 Google Trends의 공통 상위 10개 키워드:", common_keywords[:10])
