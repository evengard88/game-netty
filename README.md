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

Данный проект является тестовым, в репозитории находится несколько разных веток с разными версиями,
каждая из которых решает свою задачу.

<!-- GETTING STARTED -->
## <a id="getting-started"></a>Начало

### <a id="prerequisites"></a>Пререквизиты

Для запуска сервиса потребуются:
* Git
* Docker 
* Telnet клиент
* Свободный порт 8080

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
2. Переключиться на нужную ветку
   ```sh
   git checkout <branch-name>
   ```
3. Создать docker-image
   ```sh
   docker build -t rpc-demo .
   ```
4. Создать docker-container. Потребуется свободный порт, в данном случае 8080
   ```sh
   docker run --rm -it -p 8080:8080 rpc-demo:latest
   ```
<p align="right">(<a href="#readme-top">back to top</a>)</p>

### <a id="connect"></a> Подключение

Чтоб подключиться запускается telnet с адресом игрового сервера и портом 8080

   ```sh
   telnet <server-address> 8080
   ```
   Например, при локальном запуске надо выполнить команду:

   ```sh
   telnet localhost 8080
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

Данная версия является переработанной версией наивного подхода.

Тут все также используется Netty, однако, в отличие от предыдущего способа, слой логики
игры отделан от слоя сетевой обработки, выделена своя доменная модель и свои сервисы, что положительно
сказывается на возможности тестированя. 

В реализции использовались отдельные обработчики состояний игры, а также были задействованы некоторые элементы DDD.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### <a id="developing"/>Развитие
Фундаментальным недостаткой является то, что созданный сервис все так же невозможно масштабировать горизонтально, тк он обладает состоянием.

В следующих версиях это ограничение будет обойдено.

## License

Distributed under the Apache License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Contact

Elijah Kurenkov - ikurenkov88@gmail.com

Project Link: [https://github.com/evengard88/game-netty](https://github.com/evengard88/game-netty)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[product-screenshot]: images/demo.png