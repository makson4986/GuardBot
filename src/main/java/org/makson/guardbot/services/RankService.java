//package org.makson.dsbot.services;
//
//import lombok.RequiredArgsConstructor;
//import org.makson.dsbot.dto.RankDto;
//import org.makson.dsbot.mappers.RankMapper;
//import org.makson.dsbot.models.Rank;
//import org.makson.dsbot.repositories.RankRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RankService {
//    private final RankRepository rankRepository;
//    private final RankMapper mapper;
//
//    public RankDto getRank(String name) {
//        Rank rank = rankRepository.findByName(name);
//        return mapper.mapRank(rank);
//    }
//}
