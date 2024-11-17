package com.ganzithon.Hexfarming.domain.experience;

import com.ganzithon.Hexfarming.domain.experience.dto.fromServer.AbilityListServerDto;
import com.ganzithon.Hexfarming.domain.experience.dto.fromServer.ExperienceServerDto;
import com.ganzithon.Hexfarming.domain.experience.dto.fromServer.TierListServerDto;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/experience")
@RestController
public class ExperienceController {

    @Autowired
    ExperienceService experienceService;

    @Tag(name = "경험치 관련")
    @Operation(summary = "역량 목록 조회", description = "6개의 역량 목록을 출력한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AbilityListServerDto.class)))),
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getAbilityList")
    public List<AbilityListServerDto> getAbilityList() {
        return experienceService.getAbilityList();
    }

    @Tag(name = "경험치 관련")
    @Operation(summary = "계급 목록 조회", description = "모든 계급 목록을 출력한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TierListServerDto.class)))),
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getTierList")
    public List<TierListServerDto> getTierList() {
        return experienceService.getTierList();
    }

    @Tag(name = "경험치 관련")
    @Operation(summary = "자신의 경험치 조회", description = "현재 요청한 유저의 역량 6개에 대한 경험치 정보를 조회한다.\n\nAbility 목록: LEADERSHIP(리더십), PROBLEM_SOLVING(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)\n\nTier 목록: TIER1_1(최하) ~ TIER5_5(최상)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExperienceServerDto.class)))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저 id", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getMyExperiences")
    public List<ExperienceServerDto> getMyExperiences() {
        return experienceService.getMyExperiences();
    }

    @Tag(name = "경험치 관련")
    @Operation(summary = "특정 유저의 경험치 조회", description = "특정 유저의 역량 6개에 대한 경험치 정보를 조회한다.\n\nAbility 목록: LEADERSHIP(리더십), PROBLEM_SOLVING(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)\n\nTier 목록: TIER1_1(최하) ~ TIER5_5(최상)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExperienceServerDto.class)))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저 id", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getExperiences/{userId}")
    public List<ExperienceServerDto> getExperiences(@PathVariable int userId) {
        return experienceService.getExperiences(userId);
    }

    @Tag(name = "경험치 관련")
    @Operation(summary = "특정 유저의 특정 역량에 대한 경험치 조회", description = "특정 유저의 특정 역량에 대한 경험치 정보를 조회한다.\n\nAbility 목록: LEADERSHIP(리더십), PROBLEM_SOLVING(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)\n\nTier 목록: TIER1_1(최하) ~ TIER5_5(최상)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceServerDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저 id", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getMyAbilityExperiences/{ability}")
    public ExperienceServerDto getMyAbilityExperiences(@PathVariable Ability ability) {
        return experienceService.getMyAbilityExperiences(ability);
    }

    @Tag(name = "경험치 관련")
    @Operation(summary = "특정 유저의 특정 역량에 대한 경험치 조회", description = "특정 유저의 특정 역량에 대한 경험치 정보를 조회한다.\n\nAbility 목록: LEADERSHIP(리더십), PROBLEM_SOLVING(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)\n\nTier 목록: TIER1_1(최하) ~ TIER5_5(최상)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceServerDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저 id", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getAbilityExperiences/{ability}/{userId}")
    public ExperienceServerDto getAbilityExperiences(@PathVariable Ability ability, @PathVariable int userId) {
        return experienceService.getAbilityExperiences(ability, userId);
    }
}
