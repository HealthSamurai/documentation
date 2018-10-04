# CodeSystem

## Overview

A [CodeSystem](https://www.hl7.org/fhir/codesystem.html) resource specifies a set of Concepts included in this code system. 

Aidbox подразумевает раздельное создание ресурса CodeSystem и составляющего его набора концептов. Это означает что ресурс CodeSystem описывает только мета информацию кодовой системы: url, name, publisher.... . В то время как сами Concept ресурсы описывают содержимое этой кодовой системы, и связываются с кодовой системой через Concept.system атрибут который равен CodeSystem.url.

При этом, для поддержания совместимости с FHIR, мы разрешаем запись CodeSystem ресурса в включенным в него списком концептов. В момент сохранения CodeSystem, если в нем перечислены Concept, Aidbox сохраняет переданные Concepts в виде отдельных ресурсов, а сам ресурс CodeSystem сохраняется без concept атрибута. Данный способ создания CodeSystem  может использоваться при создании небольших справочников \(как правило, не более 100 концептов \). В случае если ваша кодовая система большая, Aidbox настоятельно рекомендует отдельно создать CodeSystem ресурс и по частям загружать Concept.

## CRUD

### Create

Разбить CodeSystem на его описание и список концептов. Сохранить CodeSystem и сами концепты

### Read

Отдавать только мета информацию  **ИЛИ**  в clojure собирать все concepts ????

### Update

Обновляем сам CodeSystem,  помечаем все старые Concept как deprecated = true, вставляем новые concept с статусом deprecated = false

### Delete

Удаление самого CodeSystem и Concepts where system = CodeSystem.url ......



