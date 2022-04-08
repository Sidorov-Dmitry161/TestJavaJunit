Инструкция по запуску
1. Для запуска приложения, в первую очередь необходимо запустить docker-compose. 
Для этого необходимо перейти в папку с проектом, вызвать git Bash here и написать docker-compose up.
2. Перейти в диекторию src/test/java/api и запустить тесты из папок negative и positive.
3. В папке target появится папка allure-results.
4. Открыть терминал и ввести mvn allure:serve или открыть окно Maven->Plugins->allure->allure:serve.
