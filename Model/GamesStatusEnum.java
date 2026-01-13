package DesafioSudokuDIO.Model;

public enum GamesStatusEnum {
    NOW_STARTED("Não iniciado"),
    COMPLETE("Completo"),
    INCOMPLETE("Incompleto");

    private String label;

    public String getLabel() {
        return label;
    }

    GamesStatusEnum(final String label){
        this.label = label;
    }
}
