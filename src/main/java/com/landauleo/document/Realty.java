package com.landauleo.document;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor //default constructor is required by the JSON serialization layer
public class Realty {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private BigInteger price;

    private String chatId;

    private Boolean isPaused = false;

    private Boolean isClosed = false;

    private String url;

    private Date createdAt;

    private Date updatedAt;

}
