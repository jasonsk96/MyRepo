package com.example.TwitterSearchApp.SMA_Adapter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
class TweetModelAssembler implements RepresentationModelAssembler<Tweet, EntityModel<Tweet>> {

  @Override
  public EntityModel<Tweet> toModel(Tweet tweet) {

    return EntityModel.of(tweet); //
        //linkTo(methodOn(TwitterController.class).one(tweet.getId())).withSelfRel());
       // linkTo(methodOn(TwitterController.class).all()).withRel("tweets"));
  }
}