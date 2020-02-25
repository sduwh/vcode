package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document("about")
public class About {
    @JsonIgnore
    @Id
    private ObjectId id;

    @NotNull(message = "doc is required")
    @Field("doc")
    private String doc;

    public ObjectId getId() {
        return id;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public About(String doc) {
        this.doc = doc;
    }

    public About() {
        this.doc = "";
    }
}
