# GNSS Positioning with Machine Learning and Particle Filter

## Overview

This project focuses on enhancing **GNSS positioning accuracy** in urban environments by integrating **Machine Learning** (ML) with a **Particle Filter**. Traditional GNSS systems face challenges in urban areas due to signal reflections and obstructions, leading to **Non-Line-of-Sight (NLOS)** signals and positioning errors. Our solution leverages **LOS/NLOS classification** using a **Random Forest** machine learning model, combined with particle filtering for more accurate position estimation.


## Problem Statement

In urban environments, GNSS signals often get obstructed or reflected by buildings, causing **NLOS** signals that lead to inaccurate positioning. The aim of this project is to classify these signals into LOS or NLOS and use this information to filter out unreliable signals, thereby improving position estimation.

## Solution Approach

1. **LOS/NLOS Classification**: A **Random Forest** model classifies satellite signals as either **LOS** or **NLOS** based on attributes like signal strength, azimuth, and elevation.
2. **Particle Filtering**: We simulate multiple potential user positions using a **Particle Filter**. These positions (or particles) are updated based on the machine learning model’s predictions, gradually refining the estimated location.
3. **Improved Position Estimation**: The system improves positioning accuracy by ignoring signals classified as NLOS and focusing on reliable LOS signals.

## Workflow

1. **Data Collection**: 
   - We collect satellite data (e.g., azimuth, elevation, signal-to-noise ratio).
   - A labeled dataset of LOS/NLOS signals is used to train the Random Forest classifier.

2. **Model Training**: 
   - The **Random Forest classifier** is trained using labeled LOS/NLOS data.
   
3. **Real-time GNSS Simulation**: 
   - During GNSS signal acquisition, each satellite signal is classified as LOS or NLOS.
   - The particle filter then refines the possible user positions based on the LOS/NLOS classification.

4. **Positioning Accuracy**: 
   - After several iterations, the particle filter provides a final, more accurate estimated location.




