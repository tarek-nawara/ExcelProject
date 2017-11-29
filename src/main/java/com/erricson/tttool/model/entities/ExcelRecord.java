package com.erricson.tttool.model.entities;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class ExcelRecord {
    private final String title;
    private final String description;
    private final String number;
    private final String aci;
    private String titleComment;
    private String descriptionComment;
    private String criteria;

    private ExcelRecord(final String title,
                        final String description,
                        final String number,
                        final String aci) {
        this.title = title;
        this.description = description;
        this.number = number;
        this.aci = aci;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }

    public String getAci() {
        return aci;
    }

    public String getTitleComment() {
        return titleComment;
    }

    public String getDescriptionComment() {
        return descriptionComment;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setTitleComment(String titleComment) {
        this.titleComment = titleComment;
    }

    public void setDescriptionComment(String descriptionComment) {
        this.descriptionComment = descriptionComment;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExcelRecord that = (ExcelRecord) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(number, that.number) &&
                Objects.equals(aci, that.aci);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, number, aci);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("description", description)
                .add("number", number)
                .add("aci", aci)
                .add("titleComment", titleComment)
                .add("descriptionComment", descriptionComment)
                .add("criteria", criteria)
                .toString();
    }

    public static ExcelRecordBuilder anExcelRecord() {
        return new ExcelRecordBuilder();
    }

    public static final class ExcelRecordBuilder {
        private String title;
        private String description;
        private String number;
        private String aci;

        private ExcelRecordBuilder() {
        }

        public ExcelRecordBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public ExcelRecordBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public ExcelRecordBuilder number(final String number) {
            this.number = number;
            return this;
        }

        public ExcelRecordBuilder aci(final String aci) {
            this.aci = aci;
            return this;
        }

        public ExcelRecord build() {
            return new ExcelRecord(title, description, number, aci);
        }
    }
}
