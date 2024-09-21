import os
import torch
import pandas as pd
from transformers import AutoTokenizer, AutoModelForCausalLM
import nltk

nltk.data.path.append(r'C:\Users\pc\AppData\Roaming\nltk_data')
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
import string

# NLTK 데이터 다운로드 (최초 실행 시)
nltk.download('punkt')
nltk.download('stopwords')


# 텍스트 전처리 함수
def preprocess_text(text):
    text = text.lower()
    text = text.translate(str.maketrans('', '', string.punctuation))
    tokens = word_tokenize(text)
    stop_words = set(stopwords.words('english'))
    tokens = [word for word in tokens if word not in stop_words]
    return ' '.join(tokens)


# 키워드 추출 함수
def extract_keywords_from_csv(file_path, num_keywords=20):
    df = pd.read_csv(file_path)
    text_data = ' '.join(df['key'].astype(str).tolist())
    processed_text = preprocess_text(text_data)

    model_id = 'MLP-KTLim/llama3-Bllossom'
    tokenizer = AutoTokenizer.from_pretrained(model_id)
    model = AutoModelForCausalLM.from_pretrained(model_id)

    # 모델을 CUDA 장치에 로드할 경우
    if torch.cuda.is_available():
        model.to('cuda')

    model.eval()

    PROMPT = '''You are a helpful AI assistant, you'll need to answer users' queries in a friendly and accurate manner.'''
    instruction = "Extract 20 keywords from the following text:\n\n" + processed_text

    messages = [
        {"role": "system", "content": f"{PROMPT}"},
        {"role": "user", "content": f"{instruction}"}
    ]

    input_ids = tokenizer.apply_chat_template(
        messages,
        add_generation_prompt=True,
        return_tensors="pt"
    )

    if torch.cuda.is_available():
        input_ids = input_ids.to('cuda')

    attention_mask = torch.ones_like(input_ids)

    try:
        outputs = model.generate(
            input_ids,
            attention_mask=attention_mask,
            max_length=2048,
            eos_token_id=tokenizer.eos_token_id,
            do_sample=True,
            temperature=0.6,
            top_p=0.9,
            repetition_penalty=1.1
        )
    except RuntimeError as e:
        print(f"Runtime error during generation: {e}")
        return []

    output_text = tokenizer.decode(outputs[0][input_ids.shape[-1]:], skip_special_tokens=True)
    keywords = output_text.split()[:num_keywords]
    return keywords


# 사용 예시
file_path = r'C:\Users\pc\Desktop\project\NewKey_genie\newkey_key_news.csv'
keywords = extract_keywords_from_csv(file_path)
print(keywords)
