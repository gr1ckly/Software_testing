# Лабораторная работа №1, Доп 1: CI pipeline (GitHub Actions)

В этом репозитории CI настроен в файле:
`.github/workflows/github-ci.yml`

## Объяснение работы Pipeline

### 1) Запуск только на Pull Request

```yaml
on:
  pull_request:
    branches:
      - main
```

**Как это работает:** pipeline запускается при создании/обновлении PR в ветку `main`.

### 2) Один job с matrix по модулям

```yaml
jobs:
  build-and-test:
    strategy:
      fail-fast: false
      matrix:
        include:
          - module_name: task-1
            pom_path: 1/pom.xml
            java_version: "25"
          - module_name: task-2
            pom_path: 2/pom.xml
            java_version: "17"
          - module_name: task-3
            pom_path: pom.xml
            java_version: "17"
```

**Как это работает:**

- один и тот же job выполняется в 3 конфигурациях;
- для каждого модуля используется свой `pom.xml`;
- для каждого модуля задается нужная версия JDK;
- `fail-fast: false` означает, что если один прогон упал, остальные все равно завершатся.

### 3) Checkout и установка Java

```yaml
steps:
  - name: Checkout repository
    uses: actions/checkout@v4

  - name: Setup JDK
    uses: actions/setup-java@v4
    with:
      distribution: temurin
      java-version: ${{ matrix.java_version }}
      cache: maven
```

**Как это работает:**

- `checkout` скачивает код репозитория на runner;
- `setup-java` ставит нужную версию JDK из matrix;
- `cache: maven` ускоряет сборку за счет кеша зависимостей.

### 4) Сборка и запуск unit-тестов

```yaml
- name: Build and run unit tests
  run: mvn -B -ntp -f "${{ matrix.pom_path }}" clean verify
```

**Как это работает:**

- `-f` выбирает конкретный `pom.xml` модуля;
- `clean verify` собирает проект и запускает тесты;
- если тесты падают, job получает статус `failed`.