import pandas as pd
import torch
from transformers import BertModel, BertTokenizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

# KoBERT 모델과 토크나이저 로드
model_name = "monologg/kobert"
tokenizer = BertTokenizer.from_pretrained(model_name)
model = BertModel.from_pretrained(model_name)

# GPU가 있으면 GPU를 사용
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = model.to(device)

# CSV 파일 읽기
file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\top_keywords_tfidf_mrc.csv'
data = pd.read_csv(file_path, encoding='utf-8-sig')

# 텍스트 임베딩 함수
def get_sentence_embedding(sentence):
    inputs = tokenizer(sentence, return_tensors='pt', truncation=True, padding=True)
    inputs = {key: value.to(device) for key, value in inputs.items()}
    with torch.no_grad():
        outputs = model(**inputs)
    embeddings = outputs.last_hidden_state[:, 0, :].cpu().numpy()
    return embeddings

# 문서와 키워드 임베딩을 계산하고, 문맥 기반 키워드를 추출
def extract_keywords_bert(texts, top_n=10):
    text_embeddings = np.vstack([get_sentence_embedding(text) for text in texts])
    keywords = []
    for text, embedding in zip(texts, text_embeddings):
        words = text.split()
        word_embeddings = np.vstack([get_sentence_embedding(word) for word in words])
        similarities = cosine_similarity(embedding.reshape(1, -1), word_embeddings).flatten()
        top_indices = similarities.argsort()[-top_n:][::-1]
        top_words = [words[i] for i in top_indices]
        keywords.append(top_words)
    return keywords

# title, who, what 열을 결합한 텍스트에서 키워드 추출
data['combined'] = data[['title', 'who', 'what','keywords']].apply(lambda x: ' '.join(x.dropna()), axis=1)
data['keywords_bert'] = extract_keywords_bert(data['combined'].tolist())

# 결과 저장
result_df = data[['title', 'who', 'what', 'keywords','keywords_bert']]
result_df.to_csv('top_keywords_bert_tfidf_mrc.csv', index=False, encoding='utf-8-sig')
