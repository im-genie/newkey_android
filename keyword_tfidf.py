import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from konlpy.tag import Okt

# 파일 경로
file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\newkey_mrc.csv'

# 데이터 읽기
try:
    data = pd.read_csv(file_path, encoding='utf-8')
except UnicodeDecodeError:
    try:
        data = pd.read_csv(file_path, encoding='cp949')
    except UnicodeDecodeError:
        data = pd.read_csv(file_path, encoding='euc-kr')

# 필요한 열 확인
print(data.columns)

# 여기서 처리할 열 이름을 지정합니다.
target_columns = ['title', 'who', 'what']

# 텍스트 전처리
okt = Okt()

stopwords_file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\stopwords-ko.txt'
with open(stopwords_file_path, 'r', encoding='utf-8') as file:
    korean_stopwords = set(file.read().split())

def preprocess(text):
    words = [word for word in okt.nouns(text) if word not in korean_stopwords and len(word) > 1]
    return ' '.join(words)

# 각 열에 대해 전처리 수행
for column in target_columns:
    data[column] = data[column].apply(preprocess)

# 모든 텍스트를 결합
data['combined'] = data[target_columns].apply(lambda x: ' '.join(x), axis=1)

# TF-IDF 계산
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(data['combined'])
feature_names = vectorizer.get_feature_names_out()

# TF-IDF 점수에 따라 키워드 추출
def extract_keywords_tfidf(row, top_n=30):
    row_tfidf = tfidf_matrix[row]
    sorted_indices = row_tfidf.toarray().flatten().argsort()[::-1]
    top_indices = sorted_indices[:top_n]
    keywords = [feature_names[i] for i in top_indices]
    return keywords

data['keywords'] = data.index.map(lambda x: extract_keywords_tfidf(x))

# 결과 저장
keywords_df = data[['title', 'who', 'what', 'keywords']]
keywords_df.to_csv('top_keywords_tfidf_mrc.csv', index=False, encoding='utf-8-sig')

print(keywords_df.head())
