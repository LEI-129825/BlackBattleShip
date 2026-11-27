# BlackBattleShip

**Engenharia de Software · Ficha laboratorial nº 5 · LEI 2026/2027**

> 🎬 **Vídeo de demonstração:** *(link do YouTube a adicionar no final da Parte 2)*

## Membros do Grupo

| Nome          | Número | GitHub                                      |
|---------------|--------|---------------------------------------------|
| Pedro Coelho  | 129825 | [LEI-129825](https://github.com/LEI-129825) |
| Martim Barros | 129873 | [LEI-129873](https://github.com/LEI-129873) |
| João Almeida  | 129862 | [LEI-129862](https://github.com/LEI-129862) |
| Gonçalo Sena  | 104893 | [LEI-104893](https://github.com/LEI-104893) |

---

## Product Backlog – Batalha Naval Online

> **Sistema em teste:** [papergames.io/en/battleship](https://papergames.io/en/battleship)
> **Formato:** user stories Scrum. Cada story tem uma descrição do contexto e critérios de aceitação em *Dado / Quando / Então*, que servem de base aos testes de aceitação automatizados.

### Legenda

| Campo | Significado |
|-------|-------------|
| **Prioridade** | MoSCoW: 🔴 *Must* · 🟠 *Should* · 🟡 *Could* |
| **SP** | Story points (escala de Fibonacci: 1, 2, 3, 5, 8) |
| **CA** | Critério de aceitação |

### Resumo

| ID | User story | Épico | Prioridade | SP |
|----|-----------|-------|:---:|:---:|
| [US1](#us1--jogar-como-convidado) | Jogar como convidado | Acesso | 🔴 | 2 |
| [US2](#us2--escolher-um-nickname) | Escolher um nickname | Acesso | 🟠 | 2 |
| [US3](#us3--jogar-contra-o-robot) | Jogar contra o robot | Modos de jogo | 🔴 | 3 |
| [US4](#us4--jogar-com-um-amigo) | Jogar com um amigo | Modos de jogo | 🟠 | 5 |
| [US5](#us5--jogar-online-com-um-jogador-aleatório) | Jogar online com um jogador aleatório | Modos de jogo | 🟡 | 3 |
| [US6](#us6--ver-o-tabuleiro-e-a-frota) | Ver o tabuleiro e a frota | Tabuleiro | 🔴 | 2 |
| [US7](#us7--colocação-automática-dos-navios) | Colocação automática dos navios | Tabuleiro | 🔴 | 3 |
| [US8](#us8--disparar-sobre-o-adversário) | Disparar sobre o adversário | Mecânica | 🔴 | 3 |
| [US9](#us9--ver-o-resultado-do-disparo) | Ver o resultado do disparo | Mecânica | 🔴 | 3 |
| [US10](#us10--afundar-navios-e-terminar-a-partida) | Afundar navios e terminar a partida | Mecânica | 🔴 | 5 |
| [US11](#us11--usar-o-míssil-grande) | Usar o míssil grande | Armas | 🟠 | 3 |
| [US12](#us12--apanhar-presentes-com-armas-especiais) | Apanhar presentes com armas especiais | Armas | 🟡 | 5 |
| [US13](#us13--consultar-a-classificação-diária) | Consultar a classificação diária | Classificações | 🟠 | 2 |
| [US14](#us14--consultar-o-ranking-ranka) | Consultar o ranking RANKA | Classificações | 🟡 | 2 |
| [US15](#us15--criar-um-torneio-privado) | Criar um torneio privado | Torneios | 🟠 | 5 |
| [US16](#us16--ver-os-meus-torneios) | Ver os meus torneios | Torneios | 🟡 | 2 |
| [US17](#us17--ler-as-regras-do-jogo) | Ler as regras do jogo | Ajuda | 🔴 | 1 |
| [US18](#us18--consultar-as-perguntas-frequentes) | Consultar as perguntas frequentes | Ajuda | 🟠 | 1 |
| [US19](#us19--navegar-para-outros-jogos) | Navegar para outros jogos | Navegação | 🟡 | 1 |

**Total:** 19 user stories · 53 story points (🔴 22 · 🟠 18 · 🟡 13)

---

### Épico A – Acesso

#### US1 – Jogar como convidado
**Como** visitante, **quero** jogar sem criar conta, **para** começar uma partida de imediato, sem fornecer dados pessoais.

**Descrição:** O site permite jogar como convidado. Ao escolher um modo de jogo pela primeira vez, só é pedido um nickname; não há registo nem login obrigatórios.

**Critérios de aceitação**
- [ ] **CA1.1** – **Dado** que não tenho sessão iniciada, **quando** abro a página do jogo, **então** os botões *Play with a friend*, *Play vs robot* e *Play online* estão visíveis e ativos.
- [ ] **CA1.2** – **Dado** que não tenho conta, **quando** escolho um modo de jogo, **então** só me é pedido um nickname, sem formulário de registo nem de login.
- [ ] **CA1.3** – **Dado** que estou a jogar como convidado, **quando** a partida começa, **então** o meu nickname aparece no topo do ecrã de jogo.

#### US2 – Escolher um nickname
**Como** jogador, **quero** escolher o meu nickname, **para** ser reconhecido pelos meus colegas e adversários.

**Descrição:** Na primeira vez que o jogador escolhe um modo de jogo, abre a janela *Who are you?* com o campo *Nickname* e o aviso *Please choose a respectful username*. Depois de confirmado, o nickname passa a aparecer no ecrã de jogo e no menu lateral, no lugar do botão *Login*.

**Critérios de aceitação**
- [ ] **CA2.1** – **Dado** que ainda não escolhi nickname, **quando** clico num modo de jogo, **então** abre a janela *Who are you?* com o campo *Nickname*.
- [ ] **CA2.2** – **Dado** que a janela *Who are you?* está aberta, **enquanto** o campo *Nickname* está vazio, **então** o botão *Continue* não está ativo.
- [ ] **CA2.3** – **Dado** que escrevo um nickname, **quando** clico em *Continue*, **então** a partida começa e o meu nickname aparece no topo do ecrã de jogo.
- [ ] **CA2.4** – **Dado** que escolhi um nickname, **quando** volto à página do jogo, **então** o menu lateral mostra o meu nickname em vez do botão *Login*.

### Épico B – Modos de jogo

#### US3 – Jogar contra o robot
**Como** jogador, **quero** jogar contra o computador, **para** praticar a qualquer hora, sem depender de outras pessoas.

**Descrição:** O botão *Play vs robot* abre de imediato uma sala de jogo (`/en/r/<código>`) contra o robot *Paper Man*. O topo do ecrã mostra os dois jogadores, cada um com um relógio de 3 minutos.

**Critérios de aceitação**
- [ ] **CA3.1** – **Dado** que abro a página do jogo, **então** existe o botão *Play vs robot*.
- [ ] **CA3.2** – **Dado** que já tenho nickname, **quando** clico em *Play vs robot*, **então** o ecrã de jogo é mostrado em menos de 10 segundos, num endereço do tipo `/en/r/<código>`.
- [ ] **CA3.3** – **Dado** que a partida começou, **então** o topo do ecrã mostra o meu nickname e o robot *Paper Man*, cada um com um relógio que começa em 03:00.
- [ ] **CA3.4** – **Dado** que a partida começou e é a minha vez, **então** a grelha do adversário mostra a indicação *Attack your opponent!*.
- [ ] **CA3.5** – **Dado** que ainda não disparei, **então** existe o botão *Abort game*; **quando** já disparei, **então** passa a existir o botão *Resign*.

#### US4 – Jogar com um amigo
**Como** jogador, **quero** convidar um amigo através de um link, **para** jogarmos um contra o outro.

**Descrição:** Ao escolher *Play with a friend*, é criada uma sala com um link de convite (`/en/r/<sala>/<código>`) e um código QR. Quem abre o link vê *<nickname> wants to play with you!*, com o código secreto já preenchido, e entra com o botão *Play*. Nenhum dos dois precisa de conta.

**Critérios de aceitação**
- [ ] **CA4.1** – **Dado** que abro a página do jogo, **então** existe o botão *Play with a friend*.
- [ ] **CA4.2** – **Dado** que estou na página do jogo, **quando** clico em *Play with a friend*, **então** são mostrados o link da sala, um código QR e o botão *Leave room*.
- [ ] **CA4.3** – **Dado** que outro jogador abre o link noutro browser, **então** vê a mensagem *<o meu nickname> wants to play with you!* e o botão *Play*.
- [ ] **CA4.4** – **Dado** que o amigo clica em *Play* e escolhe um nickname, **então** a partida começa para os dois e cada um vê o nickname do outro no topo do ecrã.

#### US5 – Jogar online com um jogador aleatório
**Como** jogador, **quero** ser emparelhado com um jogador aleatório, **para** jogar contra pessoas reais quando não tenho amigos disponíveis.

**Descrição:** O botão *Play online with a random player* coloca o jogador numa fila de espera (`/en/q/<código>`) até ser encontrado um adversário. Os testes devem abandonar a partida logo a seguir, com *Abort game*, para não prejudicar jogadores reais.

**Critérios de aceitação**
- [ ] **CA5.1** – **Dado** que abro a página do jogo, **então** existe o botão *Play online with a random player*.
- [ ] **CA5.2** – **Dado** que clico nesse botão, **então** é mostrada a mensagem *Finding a random player...*, com o tempo de espera estimado e o botão *Cancel*.
- [ ] **CA5.3** – **Dado** que estou à procura de adversário, **quando** clico em *Cancel*, **então** saio da fila de espera.
- [ ] **CA5.4** – **Dado** que estou à procura de adversário, **quando** é encontrado um jogador, **então** a partida começa e o nome do adversário aparece no topo do ecrã.

### Épico C – Tabuleiro

#### US6 – Ver o tabuleiro e a frota
**Como** jogador, **quero** ver a minha grelha e a do adversário, **para** acompanhar o estado da partida.

**Descrição:** Cada jogador tem uma grelha de 10×10 casas com uma frota de cinco navios (5, 4, 3, 3 e 2 casas, 17 no total). Em ecrãs largos, as duas grelhas aparecem lado a lado (*Your boats* e a grelha do adversário). Em ecrãs estreitos, aparece uma de cada vez, conforme a vez de quem joga. Os navios do adversário ficam escondidos.

**Critérios de aceitação**
- [ ] **CA6.1** – **Dado** que a partida começou numa janela de pelo menos 1280 px de largura, **então** as duas grelhas aparecem lado a lado e a minha tem o título *Your boats*.
- [ ] **CA6.2** – **Dado** que a partida começou, **então** cada grelha tem 10 linhas e 10 colunas (100 casas).
- [ ] **CA6.3** – **Dado** que a partida começou, **então** vejo os meus 5 navios, que ocupam no total 17 casas.
- [ ] **CA6.4** – **Dado** que a partida começou, **então** nenhuma casa da grelha do adversário mostra navios.

#### US7 – Colocação automática dos navios
**Como** jogador, **quero** que os navios sejam colocados automaticamente, **para** não perder tempo na preparação da partida.

**Descrição:** Ao contrário da versão clássica, o jogador não posiciona os navios à mão: o sistema distribui-os aleatoriamente no início de cada partida, dentro da grelha e sem sobreposições.

**Critérios de aceitação**
- [ ] **CA7.1** – **Dado** que inicio uma partida, **então** os meus navios já estão colocados na grelha, sem nenhuma ação minha.
- [ ] **CA7.2** – **Dado** que os navios foram colocados, **então** nenhum navio sai da grelha nem se sobrepõe a outro.
- [ ] **CA7.3** – **Dado** que inicio duas partidas seguidas, **quando** comparo as casas ocupadas pelos meus navios, **então** a disposição é diferente entre elas.

### Épico D – Mecânica de jogo

#### US8 – Disparar sobre o adversário
**Como** jogador, **quero** disparar sobre uma casa da grelha adversária, **para** tentar localizar e afundar os navios.

**Descrição:** Na sua vez, o jogador clica numa casa da grelha do adversário para disparar. Só são aceites disparos na vez do jogador e sobre casas ainda não atingidas.

**Critérios de aceitação**
- [ ] **CA8.1** – **Dado** que é a minha vez, **quando** clico numa casa da grelha adversária ainda não atingida, **então** o disparo é registado e a casa fica marcada.
- [ ] **CA8.2** – **Dado** que uma casa já foi atingida, **quando** clico novamente nela, **então** não é feito um novo disparo.
- [ ] **CA8.3** – **Dado** que é a vez do adversário (a grelha adversária mostra *Your opponent's boats*, e não *Attack your opponent!*), **quando** clico numa casa dessa grelha, **então** a casa não fica marcada.
- [ ] **CA8.4** – **Dado** que é a minha vez, **quando** clico na minha própria grelha, **então** não é feito nenhum disparo.

#### US9 – Ver o resultado do disparo
**Como** jogador, **quero** saber de imediato se acertei ou falhei, **para** decidir a próxima jogada.

**Descrição:** Depois de cada disparo, a casa atingida muda de aspeto: fogo, se acertou num navio, ou um ponto escuro, se falhou. Quem acerta pode disparar de novo; quem falha passa a vez. O mesmo acontece na minha grelha quando o adversário dispara.

**Critérios de aceitação**
- [ ] **CA9.1** – **Dado** que disparo e acerto num navio, **então** a casa mostra o símbolo de fogo.
- [ ] **CA9.2** – **Dado** que disparo e falho, **então** a casa fica marcada como falhada (ponto escuro, diferente das casas por atingir).
- [ ] **CA9.3** – **Dado** que disparo, **então** o resultado aparece em menos de 2 segundos.
- [ ] **CA9.4** – **Dado** que acerto num navio, **então** continua a ser a minha vez e posso disparar novamente.
- [ ] **CA9.5** – **Dado** que falho, **então** a vez passa para o adversário.
- [ ] **CA9.6** – **Dado** que o adversário dispara sobre a minha grelha, **então** a casa atingida fica marcada como acerto ou como falhanço.

#### US10 – Afundar navios e terminar a partida
**Como** jogador, **quero** saber quando um navio é afundado e quando a partida termina, **para** acompanhar o progresso e conhecer o resultado.

**Descrição:** Um navio é afundado quando todas as suas casas são atingidas e passa a ser mostrado com caveiras. Vence quem afundar primeiro toda a frota adversária. O jogador também pode desistir com *Resign*, que pede confirmação.

**Critérios de aceitação**
- [ ] **CA10.1** – **Dado** que todas as casas de um navio são atingidas, **então** essas casas passam a mostrar uma caveira (navio afundado).
- [ ] **CA10.2** – **Dado** que afundo todos os navios do adversário, **então** é mostrado o resultado de vitória.
- [ ] **CA10.3** – **Dado** que o adversário afunda todos os meus navios, **então** é mostrado o resultado de derrota.
- [ ] **CA10.4** – **Dado** que já disparei, **quando** clico em *Resign*, **então** é pedida confirmação (*Are you sure you want to continue?*) com os botões *Cancel* e *Resign*.
- [ ] **CA10.5** – **Dado** que confirmo a desistência num jogo contra o robot, **então** a partida termina e volto à página do jogo.

### Épico E – Armas

#### US11 – Usar o míssil grande
**Como** jogador, **quero** usar o míssil grande, **para** atingir 5 casas em cruz de uma só vez e encontrar navios mais depressa.

**Descrição:** Por baixo da grelha do adversário há quatro botões de armas: míssil simples (selecionado por omissão e ilimitado), míssil grande, chuva de mísseis e míssil nuclear, cada um com um contador. Cada jogador começa com 2 mísseis grandes. O míssil grande atinge a casa escolhida e as quatro adjacentes (cima, baixo, esquerda e direita); junto à margem, atinge só as casas que existem.

**Critérios de aceitação**
- [ ] **CA11.1** – **Dado** que a partida começa, **então** o míssil simples está selecionado e o contador do míssil grande mostra 2.
- [ ] **CA11.2** – **Dado** que é a minha vez, **quando** clico no botão do míssil grande, **então** esse botão fica destacado como a arma ativa.
- [ ] **CA11.3** – **Dado** que o míssil grande está selecionado, **quando** disparo numa casa interior da grelha, **então** essa casa e as 4 adjacentes ficam marcadas.
- [ ] **CA11.4** – **Dado** que uso um míssil grande, **então** o contador desce uma unidade e o míssil simples volta a ser a arma ativa.
- [ ] **CA11.5** – **Dado** que já usei os 2 mísseis grandes, **então** deixa de ser possível selecioná-lo.

#### US12 – Apanhar presentes com armas especiais
**Como** jogador, **quero** apanhar os presentes que aparecem no tabuleiro, **para** ganhar armas especiais.

**Descrição:** Depois de algumas jogadas, podem surgir presentes na grelha do adversário, mostrados com um ponto de interrogação (**?**). Ao atingir um presente, o jogador recebe uma arma extra: mais um míssil grande, uma chuva de mísseis (7 casas aleatórias) ou um míssil nuclear (área de 13 casas). Como o aparecimento é aleatório, os testes têm de jogar até surgir um presente.

**Critérios de aceitação**
- [ ] **CA12.1** – **Dado** que a partida começa, **então** os botões da chuva de mísseis e do míssil nuclear estão visíveis com o contador a 0.
- [ ] **CA12.2** – **Dado** que já foram feitas várias jogadas, **quando** surge um presente, **então** a casa mostra um ponto de interrogação.
- [ ] **CA12.3** – **Dado** que existe um presente na grelha, **quando** disparo sobre essa casa, **então** o contador de uma das armas especiais aumenta uma unidade.
- [ ] **CA12.4** – **Dado** que tenho uma chuva de mísseis, **quando** a uso, **então** são marcadas até 7 casas da grelha adversária.
- [ ] **CA12.5** – **Dado** que tenho um míssil nuclear, **quando** o uso numa casa, **então** é marcada uma área de até 13 casas à volta dela.

### Épico F – Classificações

#### US13 – Consultar a classificação diária
**Como** jogador, **quero** ver a classificação do dia, **para** comparar o meu desempenho com o de outros jogadores.

**Descrição:** A página do jogo mostra a classificação do torneio diário, com a posição, o nome e a pontuação dos melhores jogadores, e a mensagem *Daily leaderboard, ends in* seguida de uma contagem decrescente. O link *See all* abre a página completa do torneio diário.

**Critérios de aceitação**
- [ ] **CA13.1** – **Dado** que abro a página do jogo, **então** existe a classificação diária, com o texto *Daily leaderboard, ends in*.
- [ ] **CA13.2** – **Dado** que vejo a classificação, **então** a contagem decrescente está no formato hh:mm:ss e diminui com o tempo.
- [ ] **CA13.3** – **Dado** que vejo a classificação, **então** cada linha mostra a posição, o nome do jogador e a pontuação.
- [ ] **CA13.4** – **Dado** que vejo a classificação, **então** as posições começam em 1, são consecutivas e as pontuações não aumentam de uma linha para a seguinte.
- [ ] **CA13.5** – **Dado** que estou na classificação diária, **quando** clico em *See all*, **então** abre a página do torneio diário (`/en/t/<código>`).

#### US14 – Consultar o ranking RANKA
**Como** jogador, **quero** ver o ranking RANKA, **para** conhecer a classificação geral dos últimos 30 dias.

**Descrição:** O RANKA é o ranking geral do site, calculado com base nos últimos 30 dias (*General ranking since 30 days*). A página do jogo mostra os 20 primeiros, com a pontuação (*Score*), e uma ligação *Learn more* para a explicação.

**Critérios de aceitação**
- [ ] **CA14.1** – **Dado** que abro a página do jogo, **então** existe a secção *RANKA*, com o texto *General ranking since 30 days*.
- [ ] **CA14.2** – **Dado** que vejo o ranking, **então** cada linha mostra a posição, o nome do jogador e a pontuação.
- [ ] **CA14.3** – **Dado** que estou na secção RANKA, **quando** clico em *Learn more*, **então** abre a página `/en/blog/ranka`, com o título *Ranka - papergames.io*.

### Épico G – Torneios

#### US15 – Criar um torneio privado
**Como** organizador, **quero** criar um torneio privado, **para** organizar um campeonato com o meu grupo de amigos.

**Descrição:** O botão *Create tournament* abre a página *Tournament creation*, sem exigir login. O formulário tem o nome do torneio (obrigatório, com sugestões como *Battleship Friday Cup*), uma descrição opcional, o jogo (Battleship já selecionado), o modo (*Leaderboard* ou *Bracket*) e as regras das partidas (*Best of 3 · 3 minutes · 15 pts penalty*). A versão gratuita aceita até 4 jogadores. Os testes automatizados não devem clicar em *Create and share* em todas as execuções, porque isso cria torneios reais.

**Critérios de aceitação**
- [ ] **CA15.1** – **Dado** que abro a página do jogo, **então** existe o botão *Create tournament*.
- [ ] **CA15.2** – **Dado** que estou na página do jogo, **quando** clico em *Create tournament*, **então** abre a página *Tournament creation* com o jogo *Battleship* selecionado.
- [ ] **CA15.3** – **Dado** que o nome do torneio está vazio, **então** o botão *Create and share* está desativado e é mostrada a indicação *Add a name — it's what your friends will see.*
- [ ] **CA15.4** – **Dado** que escrevo um nome para o torneio, **então** o botão *Create and share* fica ativo.
- [ ] **CA15.5** – **Dado** que estou no formulário, **quando** escolho o modo *Bracket*, **então** esse modo fica selecionado no lugar de *Leaderboard*.

#### US16 – Ver os meus torneios
**Como** jogador, **quero** ver a lista dos meus torneios, **para** acompanhar aqueles que criei ou em que participo.

**Descrição:** A opção *My tournaments*, no menu lateral, abre a página `/en/t/my-tournaments`, que reúne os torneios associados ao jogador.

**Critérios de aceitação**
- [ ] **CA16.1** – **Dado** que estou no site, **então** o menu lateral tem a opção *My tournaments*.
- [ ] **CA16.2** – **Dado** que estou no site, **quando** clico em *My tournaments*, **então** abre a página com o título *My tournaments*.
- [ ] **CA16.3** – **Dado** que uso uma sessão nova, sem torneios, **quando** abro *My tournaments*, **então** é mostrada a mensagem *You don't have any tournaments yet.* e o botão *Create tournament*.

### Épico H – Ajuda e navegação

#### US17 – Ler as regras do jogo
**Como** novo jogador, **quero** ler as regras, **para** perceber como se joga e o que muda em relação à versão clássica.

**Descrição:** A página do jogo tem a secção *Rules of Battleship game online*, com as subsecções *Gifts* e *Weapons*. Explica a jogabilidade (turnos, acertos, vitória) e as armas especiais, que não existem na batalha naval clássica.

**Critérios de aceitação**
- [ ] **CA17.1** – **Dado** que abro a página do jogo, **então** existe a secção *Rules of Battleship game online*.
- [ ] **CA17.2** – **Dado** que leio as regras, **então** é explicado que a grelha é de 10×10 e que a frota tem navios de 5, 4, 3, 3 e 2 casas.
- [ ] **CA17.3** – **Dado** que leio as regras, **então** é explicado que um acerto dá direito a novo disparo e que vence quem afundar toda a frota adversária.
- [ ] **CA17.4** – **Dado** que leio as regras, **então** as secções *Gifts* e *Weapons* descrevem os presentes e as quatro armas (*Simple missile*, *Big missile*, *Missile rain* e *Nuclear missile*).

#### US18 – Consultar as perguntas frequentes
**Como** jogador, **quero** consultar a FAQ, **para** esclarecer dúvidas sem precisar de ajuda externa.

**Descrição:** No fim da página do jogo há a secção *Frequently asked questions*, com 7 perguntas, cada uma seguida da resposta, sempre visível: como ganhar, número de navios, estratégia, armas, jogar contra o computador, tamanho do tabuleiro e se o jogo é gratuito.

**Critérios de aceitação**
- [ ] **CA18.1** – **Dado** que abro a página do jogo, **então** existe a secção *Frequently asked questions*, com 7 perguntas.
- [ ] **CA18.2** – **Dado** que vejo a FAQ, **então** cada pergunta tem a resposta visível logo abaixo.
- [ ] **CA18.3** – **Dado** que leio a pergunta *Is Battleship free on papergames.io?*, **então** a resposta indica que o jogo é gratuito e que se pode jogar como convidado.

#### US19 – Navegar para outros jogos
**Como** visitante, **quero** aceder aos outros jogos do site, **para** experimentar jogos diferentes sem sair do site.

**Descrição:** O menu lateral tem a secção *Games*, com os outros jogos disponíveis (Tic Tac Toe, Connect 4, Gomoku, Chess, Checkers, entre outros). Em ecrãs estreitos, o menu abre no botão ☰. Cada entrada leva à página do respetivo jogo.

**Critérios de aceitação**
- [ ] **CA19.1** – **Dado** que abro a página do jogo, **então** o menu lateral tem a secção *Games*, com o jogo atual (*Battleship*) destacado.
- [ ] **CA19.2** – **Dado** que vejo a secção *Games*, **então** estão listados pelo menos *Tic Tac Toe*, *Connect 4*, *Gomoku*, *Chess* e *Checkers*.
- [ ] **CA19.3** – **Dado** que estou na secção *Games*, **quando** escolho um jogo, **então** abre a página desse jogo (por exemplo, `/en/connect4`), com o nome do jogo no título.

---

## Notas para os testes automatizados

Observações feitas no site, úteis para escrever as Page Object Classes:

- **Cookies:** na primeira visita aparece uma janela de consentimento; feche-a com o botão *Consent* (`button.fc-cta-consent`).
- **Nickname:** numa sessão nova, o primeiro modo de jogo escolhido abre a janela *Who are you?*. Os testes têm de preencher o campo *Nickname* e clicar em *Continue*.
- **Publicidade:** surgem anúncios em vídeo por cima da página, que podem tapar cliques. Feche-os ou clique noutras zonas.
- **Largura da janela:** acima de cerca de 1280 px, as duas grelhas aparecem lado a lado; em janelas estreitas, aparece uma de cada vez. Fixe o tamanho da janela no `@BeforeEach`.
- **Grelhas:** `table.table-board` (a primeira é a minha e a segunda a do adversário); cada casa é `td.cell-<linha>-<coluna>`, de 0 a 9.
- **Resultados:** acerto = `.hit.fire`; falhanço = `svg.no-hit`; navio afundado = imagem com `alt="Skull"`; presente = `.gift`.
- **Armas:** `button.weapon-button`, e a ativa tem a classe `is-selected`; as imagens têm `alt` igual a `missile` (míssil grande), `fragment-bomb` (chuva de mísseis) e `nuclear-bomb`.
- **Tempo:** cada jogador tem 3 minutos por partida, por isso os testes devem jogar sem pausas longas.

---

## Definition of Done

Uma user story só está concluída quando:

1. Todos os seus critérios de aceitação estão cobertos por testes de caixa-preta automatizados, executados no Chrome.
2. **Parte 1 (Selenium):** o cenário foi gravado no Selenium IDE (`TestSuite_<número>.side`, cenário `UserStoryTest<N>`) e implementado com o padrão Page Object Model: a Page Object Class em `src/main/java` e a Page Test Class em `src/test/java`, ambas com Javadoc.
3. **Parte 2 (Selenide):** existe a versão Selenide do teste, com Page Object Model e Page Factory, no pacote da test suite em `src/test/java`, e o relatório Allure foi copiado para `reports/`.
4. Os testes passam localmente com `mvn test`.
5. Foi feito um commit por user story, com o identificador na mensagem (ex.: `UserStoryTest2`).
6. Foi integrada na `main` através de Pull Request revisto por outro membro do grupo, e o Issue da test suite foi fechado.
