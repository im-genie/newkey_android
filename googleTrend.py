# connect
from pytrends.request import TrendReq
import pandas as pd

pytrends = TrendReq(hl='ko-KR', tz=540)
df=pytrends.trending_searches(pn='south_korea')
print(df)
