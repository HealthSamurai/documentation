# CodeSystem

## Overview

[https://www.hl7.org/fhir/codesystem.html](https://www.hl7.org/fhir/codesystem.html) FHIR docs

Мы разбиваем CodeSystem на описание CodeSystem и на его Concepts. По этому есть некоторые ограничения в работе CRUD над CodeSystem

## CRUD

### Create

Разбить CodeSystem на его описание и список концептов. Сохранить CodeSystem и сами концепты

### Read

Отдавать только мета информацию  **ИЛИ**  в clojure собирать все concepts ????

### Update

Обновляем сам CodeSystem,  помечаем все старые Concept как deprecated = true, вставляем новые concept с статусом deprecated = false

### Delete

Удаление самого CodeSystem и Concepts where system = CodeSystem.url ......



