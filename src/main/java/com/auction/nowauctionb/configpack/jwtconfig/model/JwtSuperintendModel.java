package com.auction.nowauctionb.configpack.jwtconfig.model;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class JwtSuperintendModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user_id")
    @OneToOne
    private UserModel user;

    private String accessToken;

    private String refreshToken;

    @CreationTimestamp
    private Timestamp createToken;

    @UpdateTimestamp
    private Timestamp modifyToken;

}
