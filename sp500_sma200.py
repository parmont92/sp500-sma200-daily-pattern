#!/usr/bin/env python

import yfinance as yf
import requests

TOKEN = open('../configs/botToken.txt').read()[:46]
MY_CHAT_ID = int(open('../configs/chatId.txt').read())

symbol = "^SPX"

data = yf.download(tickers=symbol, period="1y")

data['SMA200'] = data['Close'].rolling(window=200).mean()

sma200 = round(data['SMA200'].tail(1).iloc[-1], 2)
precio = round(data['Close'].tail(3).iloc[0], 2)
precio2 = round(data['Close'].tail(3).iloc[1], 2)
precio3 = round(data['Close'].tail(3).iloc[2], 2)

patron = (precio > precio2) and (precio2 > precio3) and (precio3 > sma200)

message = f"SP500\n{precio}\n{precio2}\n{precio3}\nSMA 200: {sma200}"

if patron:
    message += "\nSí hay patrón confirmado"
else:
    message += "\nNo hay patrón"

print(message)

url = f"https://api.telegram.org/bot{TOKEN}/sendMessage?chat_id={MY_CHAT_ID}&text={message}"
requests.get(url).json()
