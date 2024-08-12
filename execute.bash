#!/usr/bin/bash

CHAT_ID="$(cat /home/alarm/raspberry-telegram-bot/configs/chatId.txt)"
BOT_TOKEN="$(cat /home/alarm/raspberry-telegram-bot/configs/botToken.txt)"

out="$(java -jar build/libs/sp500-daily-pattern.jar)"

if [[ $out == *"Has pattern: false"* ]]; then
  curl -s -X POST https://api.telegram.org/bot"$BOT_TOKEN"/sendMessage -d chat_id="$CHAT_ID" -d text="Patrón long SÍ confirmado, SP500 por encima de la sma de 200 periodos después de tres días de retroceso.">/dev/null 2>&1
fi