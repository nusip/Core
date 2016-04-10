package kz.maks.core.front.ui;

import kz.maks.core.shared.dtos.RefDto;

public enum RefColumn implements IColumn<RefDto> {
    code("Код", false),
    title("Значение", true),
    ;

    private final String sTitle;
    private final boolean isEditable;
    private final int width;

    RefColumn(String title, boolean isEditable) {
        this.sTitle = title;
        this.isEditable = isEditable;
        this.width = IColumn.DEFAULT_WIDTH;
    }

    @Override
    public String getTitle() {
        return sTitle;
    }

    @Override
    public Class<RefDto> tableClass() {
        return RefDto.class;
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public String toString() {
        return sTitle;
    }
}
