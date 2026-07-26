with open("data/territories.js", "r", encoding="utf-8") as f:
    t = f.read()
print("data/territories.js length:", len(t))
print("First 200 chars:", t[:200])
