package org.makson.guardbot.mappers;


import org.makson.guardbot.dto.RankDto;
import org.makson.guardbot.models.Rank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RankMapper {
    RankDto mapRank(Rank rank);
}
