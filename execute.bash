#!/usr/bin/bash

CHAT_ID="$(cat /home/alarm/raspberry-telegram-bot/configs/chatId.txt)"
BOT_TOKEN="$(cat /home/alarm/raspberry-telegram-bot/configs/botToken.txt)"

out="$(java -jar build/libs/sp500-daily-pattern.jar)"

if [[ $out == *"Has pattern: true"* ]]; then
  curl -s -X POST https://api.telegram.org/bot"$BOT_TOKEN"/sendMessage -d chat_id="$CHAT_ID" -d text="Buy signal, sp500-sma200-daily-pattern.">/dev/null 2>&1
fi
