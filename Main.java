package DesafioSudokuDIO;

import DesafioSudokuDIO.Model.Board;
import DesafioSudokuDIO.Model.Space;
import DesafioSudokuDIO.Util.BoardTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static DesafioSudokuDIO.Util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        // Se nenhum argumento for passado, usa o template padrão
        Map<String, String> positions = BoardTemplate.POSITIONS;

        var option = -1;

        while (true) {
            System.out.println("\nSELECIONE UMA DAS OPÇÕES A SEGUIR");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();

        for (int row = 0; row < BOARD_LIMIT; row++) {
            spaces.add(new ArrayList<>());

            for (int col = 0; col < BOARD_LIMIT; col++) {
                String key = row + "," + col;
                String config = positions.get(key);

                if (config == null) {
                    throw new RuntimeException("Configuração faltando para: " + key);
                }

                String[] data = config.split(",");
                int expected = Integer.parseInt(data[0]);
                boolean fixed = Boolean.parseBoolean(data[1]);

                spaces.get(row).add(new Space(expected, fixed));
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar!");
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna (0-8):");
        var col = runUntilGetValidNumber(0, 8);

        System.out.println("Informe a linha (0-8):");
        var row = runUntilGetValidNumber(0, 8);

        System.out.printf("Informe o valor para [%s,%s]:\n", col, row);
        var value = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s,%s] possui valor fixo\n", col, row);
        }
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna (0-8):");
        var col = runUntilGetValidNumber(0, 8);

        System.out.println("Informe a linha (0-8):");
        var row = runUntilGetValidNumber(0, 8);

        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s,%s] possui valor fixo\n", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        var args = new Object[81];
        int pos = 0;

        for (int row = 0; row < BOARD_LIMIT; row++) {
            for (int col = 0; col < BOARD_LIMIT; col++) {
                Integer value = board.getSpaces().get(row).get(col).getActual();
                args[pos++] = value == null ? 0 : value;
            }
        }

        System.out.println("\nSeu jogo está assim:");
        System.out.printf(BOARD_TEMPLATE + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("Status: %s\n", board.getStatus().getLabel());
        System.out.println(board.hasError() ? "O jogo contém erros" : "O jogo não contém erros");
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar o jogo? (sim/não)");
        var confirm = scanner.next();

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("Jogo reiniciado.");
        }
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.gameIsFinish()) {
            System.out.println("Parabéns! Você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasError()) {
            System.out.println("Seu jogo contém erros.");
        } else {
            System.out.println("Ainda existem espaços vazios.");
        }
    }

    private static int runUntilGetValidNumber(int min, int max) {
        int value = scanner.nextInt();
        while (value < min || value > max) {
            System.out.printf("Digite um valor entre %d e %d:\n", min, max);
            value = scanner.nextInt();
        }
        return value;
    }
}
