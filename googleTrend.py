import sys
import io

# 표준 출력을 UTF-8로 설정
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

# Pytrends 코드
from pytrends.request import TrendReq

pytrends = TrendReq(hl='ko-KR', tz=540)
df = pytrends.trending_searches(pn='south_korea')
print(df)