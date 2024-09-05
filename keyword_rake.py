import pandas as pd
from rake_nltk import Rake
from konlpy.tag import Okt

# CSV 파일 읽기
file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\newkey_mrc.csv'
data = pd.read_csv(file_path, encoding='utf-8-sig')

# 텍스트 전처리
okt = Okt()

stopwords_file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\stopwords-ko.txt'
with open(stopwords_file_path, 'r', encoding='utf-8') as file:
    korean_stopwords = set(file.read().split())

def preprocess(text):
    words = [word for word in okt.nouns(text) if word not in korean_stopwords and len(word) > 1]
    return ' '.join(words)

data['processed'] = data[['title', 'who', 'what']].apply(lambda x: preprocess(' '.join(x.dropna())), axis=1)

# RAKE 키워드 추출
r = Rake(stopwords=korean_stopwords)

def extract_keywords_rake(text, top_n=30):
    r.extract_keywords_from_text(text)
    keywords = r.get_ranked_phrases()[:top_n]
    return keywords

data['keywords_rake'] = data['processed'].apply(lambda x: extract_keywords_rake(x))

# 결과 저장
result_df = data[['title', 'who', 'what', 'keywords_rake']]
result_df.to_csv('top_keywords_rake_mrc.csv', index=False, encoding='utf-8-sig')

print(result_df.head())
