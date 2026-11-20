# Практика 8, вариант 21

JDK 17+. Запуск: `sh run_selfcheck.sh`. Windows: `run_selfcheck.bat`.
Интерактивный режим: `sh run_selfcheck.sh --interactive`.

См. DECISIONS.md, test-results.txt, diagrams и отчёт. Репозиторий проекта — https://github.com/SanTW3/practice-08. Отчёт — REPORT.docx.
В части Б строго воспроизведён compareTo из задания. Для настоящего числа календарных дней нужен ChronoUnit.DAYS.between(today, start); compareTo не эквивалентен ему между месяцами и годами.
