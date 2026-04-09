# Mutation Testing Report With PIT

### Результаты до исправлений
Общий:
![alt text](image.png)

org.example.csv:
![alt text](image-1.png)

org.example.functions.equation:
![alt text](image-2.png)

org.example.functions.ln:
![alt text](image-3.png)

org.example.example.function.trig:
![alt text](image-4.png)

### Исправления
Основные классы которые можно исправить - LnFunction и SinFunction
Для LnFunction необходимо добавить ветку проверки mantissa.
Для SunFunction необходимо добавить проверки при `x == accuracy`
После добавления тестов на мантиссу потребовалось заменить if на while.

### Результаты после исправлений
Общий:
![alt text](image-5.png)

org.example.csv:
![alt text](image-6.png)

org.example.functions.equation:
![alt text](image-7.png)

org.example.functions.ln:
![alt text](image-8.png)

org.example.example.function.trig:
![alt text](image-9.png)
