package com.vulinh.data.entity;

import module java.base;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Employee implements Serializable {

  @Serial private static final long serialVersionUID = -5526812392569190667L;

  private static final String SEQUENCE_NAME = "employee_seq";
  private static final String SEQUENCE_GENERATOR = SEQUENCE_NAME + "_gen";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
  @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = SEQUENCE_NAME, allocationSize = 1)
  Long id;

  String name;
  LocalDate birthDate;

  @CreatedDate Instant createdAt;

  @LastModifiedDate Instant updatedAt;
}
