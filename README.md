<a id="readme-top"></a>

# Rock-Paper-Scissors with Netty

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Оглавление</summary>
  <ol>
    <li>
      <a href="#about-the-project">О проекте</a>
    </li>
    <li>
      <a href="#getting-started">Начало</a>
      <ul>
        <li><a href="#prerequisites">Пререквизиты</a></li>
        <li><a href="#start">Запуск сервера</a></li>
        <li><a href="#connect">Подключение</a></li>
      </ul>
    </li>
    <li><a href="#usage">Использование</a></li>
    <li><a href="#tech-details">Технические детали</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>

  </ol>
</details>

## <a id="about-the-project"></a>О проекте

![Демо][product-screenshot]


Простой сервис на Telnet, реализующий мультиплеер для игры камень-ножинцы-бумага.
В основе использует:
* Java 
* Netty
* Redis

Данный проект является тестовым, в репозитории находится несколько разных веток с разными версиями,
каждая из которых решает свою задачу.

ДАННАЯ ВЕТКА В РАЗРАБОТКЕ, ПОДРОБНЕЕ В ТЕХНИЧЕСКИХ ДЕТАЛЯХ

<!-- GETTING STARTED -->
## <a id="getting-started"></a>Начало

### <a id="prerequisites"></a>Пререквизиты

Для запуска сервиса потребуются:
* Git
* Docker 
* Telnet клиент
* Свободные порты 4000-4005 для сервиса telnet
* Свободные порты 6379 для сервиса redis

Установка соответстующего ПО зависит от используемого дистрибутива ОС.

### <a id="start"></a> Запуск сервера

Следующие шаги описывают запуск сервера:
1. Склонировать проект
   ```sh
   git clone https://github.com/evengard88/game-netty.git
   ```
   ```sh
   git fetch --all
   ```
2. Переключиться на ветку ```scalable```
   ```sh
   git checkout scalable
   ```
3. Запустить проект
   ```sh
   docker compose up
   ```
<p align="right">(<a href="#readme-top">back to top</a>)</p>

### <a id="connect"></a> Подключение

Чтоб подключиться запускается telnet с адресом игрового сервера и портом 4000

   ```sh
   telnet <server-address> 4000
   ```
   Например, при локальном запуске надо выполнить команду:

   ```sh
   telnet localhost 4000
   ```
   
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## <a id="usage"></a>Использование

Использование представляет собой игру камень, ножницы, бумага.

При начале подключения игра запросит имя, нужно любое не пустое имя латиницей.

Далее серис ищет соперника, выбирает одного из игроков и требуется выбрать одно из действий, камень ножницы или бумага.

Сопреник также выбирает свое действие.

В момент когда оба дейтвия выбраны игрой назначается победитель и проигравший, в случае ничьи игра перезапускает раунд. 


<p align="right">(<a href="#readme-top">back to top</a>)</p>

## <a id="tech-details"/>Технические детали

Данная версия представляет распределенную систему, с отдельнымм серверами для telnet клиентов и сервисами для обработки логик игры.

Базовая идея состоит в том, что сервисы сами не хранят состояние, для этого используется Redis 
в качестве базы данных, а так же для очереди сообщений и для распределенных локов. 

Сделана попытка придерживаться событийно-ориентированной архитектуры, что позволит не терять данные при даунтайме 
(не относится к текстовым клиентам, этот вопрос все еще открытый)


<p align="right">(<a href="#readme-top">back to top</a>)</p>

### <a id="developing"/>Известные проблемы
Данный код является продолжением развития идеи, но уже у учетом, что требуется масштабирование, а значит сервисы должны быть stateless.

Из известных проблем:
- Перейти с pub/sub на списки redis.
- Определить критические секции для конкурентного доступа, чтоб защитить от перезатирания/неконсистентного состояния.
- Решить, как лучше защищаться, использовать распределенный лок (если да - то какой, какой ключ и тд) или задействовать 
транзакции Redis (что в теории наверно лучше)
- Докрутить docker-compose 

### <a id="developing"/>Точки развития
Настроить nginx со sticky session для балансировки.

Перейти ли на реактивную/асинхронную архитектуру как на клиенте, так и на беке.

## License

Distributed under the Apache License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Contact

Elijah Kurenkov - ikurenkov88@gmail.com

Project Link: [https://github.com/evengard88/game-netty](https://github.com/evengard88/game-netty)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[product-screenshot]: images/demo.png