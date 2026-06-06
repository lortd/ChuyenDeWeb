package com.example.dailydictation.entity;

import com.example.dailydictation.enums.EStatusVNPay;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User user;

    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private EStatusVNPay eStatusVNPay;

}
