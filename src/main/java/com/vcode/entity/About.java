package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class About {
    @JsonIgnore
    @Id
    private ObjectId id;

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
}
