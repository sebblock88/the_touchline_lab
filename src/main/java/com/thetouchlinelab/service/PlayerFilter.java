package com.thetouchlinelab.service;

import com.thetouchlinelab.fpl.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record PlayerFilter(Integer minMinutes, Double maxPrice) {}
