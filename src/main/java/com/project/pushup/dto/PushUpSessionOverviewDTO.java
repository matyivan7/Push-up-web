package com.project.pushup.dto;

import com.project.pushup.dto.model.AllPushUpSessionModel;
import com.project.pushup.dto.model.UserPushUpSessionModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushUpSessionOverviewDTO {

    private List<UserPushUpSessionModel> userPushUpSessionModels;
    private List<AllPushUpSessionModel> allPushUpSessionModels;

}
