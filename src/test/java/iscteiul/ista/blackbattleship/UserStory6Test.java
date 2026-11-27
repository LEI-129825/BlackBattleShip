package iscteiul.ista.blackbattleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Page Test Class da user story US6 – Ver o tabuleiro e a frota.
 * <p>
 * Testes de aceitação (caixa-preta) derivados do cenário {@code UserStoryTest6}
 * do ficheiro {@code TestSuite_129873.side}. Todos os testes começam com uma
 * partida contra o robot já iniciada, numa janela com 1400 px de largura (o
 * critério CA6.1 exige pelo menos 1280 px para as grelhas ficarem lado a lado).
 * </p>
 */
class UserStory6Test {

    /** Nickname usado nos testes. */
    private static final String NICKNAME = "TesteMartim";

    /** Número de casas dos cinco navios: 5 + 4 + 3 + 3 + 2. */
    private static final int FLEET_CELLS = 17;

    private WebDriver driver;
    private UserStory6 page;

    /** Abre um browser novo, inicia uma partida contra o robot e espera pelas grelhas. */
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 900));
        page = new UserStory6(driver);
        page.open();
        page.acceptCookiesIfPresent();
        page.startRobotGameAsGuest(NICKNAME);

        assertTrue(page.waitForGameScreen(Duration.ofSeconds(10)), "O ecrã de jogo deve abrir");
        assertTrue(page.waitForBoards(), "As duas grelhas devem ser desenhadas, com os navios colocados");
    }

    /** Fecha o browser no fim de cada teste. */
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * CA6.1 – Dado que a partida começou numa janela de pelo menos 1280 px de largura,
     * então as duas grelhas aparecem lado a lado e a minha tem o título Your boats.
     */
    @Test
    @DisplayName("CA6.1 – Duas grelhas lado a lado, a minha com o título Your boats")
    void ca6_1_grelhasLadoALado() {
        assertEquals(2, page.getBoardCount(), "Devem existir duas grelhas");
        assertTrue(page.isYourBoatsTitleDisplayed(), "A minha grelha deve ter o título Your boats");
        assertTrue(page.areBoardsSideBySide(), "As grelhas devem aparecer lado a lado");
    }

    /**
     * CA6.2 – Dado que a partida começou, então cada grelha tem 10 linhas e 10 colunas
     * (100 casas).
     */
    @Test
    @DisplayName("CA6.2 – Cada grelha tem 10 linhas e 10 colunas (100 casas)")
    void ca6_2_grelhasDe10Por10() {
        for (int board : new int[]{UserStory6.MY_BOARD, UserStory6.OPPONENT_BOARD}) {
            List<Integer> cellsPerRow = page.getCellsPerRow(board);
            assertEquals(10, cellsPerRow.size(), "A grelha " + board + " deve ter 10 linhas");
            assertTrue(cellsPerRow.stream().allMatch(cells -> cells == 10),
                    "Cada linha da grelha " + board + " deve ter 10 casas, mas tem " + cellsPerRow);
            assertEquals(100, page.countCells(board), "A grelha " + board + " deve ter 100 casas");
        }
    }

    /**
     * CA6.3 – Dado que a partida começou, então vejo os meus 5 navios, que ocupam no
     * total 17 casas. Os navios contam-se pelos grupos de casas que o site distingue
     * por classe CSS.
     */
    @Test
    @DisplayName("CA6.3 – Vejo os meus 5 navios, que ocupam 17 casas")
    void ca6_3_cincoNaviosCom17Casas() {
        assertEquals(FLEET_CELLS, page.countShipCells(UserStory6.MY_BOARD),
                "Os meus navios devem ocupar 17 casas");

        var groups = page.getShipGroups(UserStory6.MY_BOARD);
        assertEquals(5, groups.size(), "Devem existir 5 navios, mas encontrei os grupos " + groups);
    }

    /**
     * CA6.4 – Dado que a partida começou, então nenhuma casa da grelha do adversário
     * mostra navios.
     */
    @Test
    @DisplayName("CA6.4 – A grelha do adversário não mostra navios")
    void ca6_4_adversarioSemNavios() {
        assertEquals(100, page.countCells(UserStory6.OPPONENT_BOARD),
                "A grelha do adversário deve ter 100 casas");
        assertEquals(0, page.countShipCells(UserStory6.OPPONENT_BOARD),
                "Nenhuma casa do adversário deve mostrar um navio");
    }
}