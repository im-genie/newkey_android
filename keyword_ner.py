import pandas as pd
from rake_nltk import Rake
import nltk

# Download the required nltk data
nltk.download('stopwords')
nltk.download('punkt')

# Specify the file path
file_path = 'c:/Users/pc/Desktop/project/NewKey_genie/newkey_key_last.csv'

# Load the CSV file with appropriate encoding
try:
    data = pd.read_csv(file_path, encoding='utf-8')
except UnicodeDecodeError:
    data = pd.read_csv(file_path, encoding='cp949')

# Initialize RAKE using the default settings
rake_nltk_var = Rake()

# Extract text data from the 'key' column
texts = data['key'].astype(str).tolist()

# Concatenate all text data into a single string
full_text = ' '.join(texts)

# Apply RAKE to extract keywords
rake_nltk_var.extract_keywords_from_text(full_text)

# Get the ranked phrases with their scores
keywords = rake_nltk_var.get_ranked_phrases_with_scores()

# Prepare to split phrases into individual words
individual_words = []
for score, phrase in keywords:
    words = phrase.split()
    for word in words:
        individual_words.append((score, word))

# Get the unique top words by highest score
unique_words = {}
for score, word in individual_words:
    if word not in unique_words or unique_words[word] < score:
        unique_words[word] = score

# Sort words by score in descending order
sorted_words = sorted(unique_words.items(), key=lambda x: x[1], reverse=True)[:30]

# Create a DataFrame to display the top 30 keywords
top_words_df = pd.DataFrame(sorted_words, columns=['Keyword', 'Score'])

# Print the DataFrame to check the result
print(top_words_df)

# Save the DataFrame to a CSV file
output_file_path = 'c:/Users/pc/Desktop/project/NewKey_genie/top_words.csv'
top_words_df.to_csv(output_file_path, index=False, encoding='utf-8-sig')
