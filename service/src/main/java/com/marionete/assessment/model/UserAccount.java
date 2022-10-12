package com.marionete.assessment.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class UserAccount {
    private Account accountInfo;
    private User userInfo;
}
