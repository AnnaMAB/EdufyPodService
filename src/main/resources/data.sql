-- GENRES
INSERT INTO genre (genre_id, name, thumbnail_url, image_url) VALUES
('11111111-1111-1111-1111-111111111111', 'Tech', 'https://cdn.example.com/genres/tech_thumb.jpg', 'https://cdn.example.com/genres/tech_image.jpg'),
('22222222-2222-2222-2222-222222222222', 'Comedy', 'https://cdn.example.com/genres/comedy_thumb.jpg', 'https://cdn.example.com/genres/comedy_image.jpg'),
('33333333-3333-3333-3333-333333333333', 'True Crime', 'https://cdn.example.com/genres/crime_thumb.jpg', 'https://cdn.example.com/genres/crime_image.jpg');

INSERT INTO podcast (podcast_id, name, description, thumbnail_url, image_url, producer_id) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'CodeTalk', 'A podcast about software development, architecture, and frameworks.', 'https://cdn.example.com/podcasts/codetalk_thumb.jpg', 'https://cdn.example.com/podcasts/codetalk_image.jpg', 'some-producer-id'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'LaughTrack', 'Comedy podcast with improv and absurd humor.', 'https://cdn.example.com/podcasts/laughtrack_thumb.jpg', 'https://cdn.example.com/podcasts/laughtrack_image.jpg', 'some-producer-id'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'CrimeLens', 'Investigating real-world crime stories from around the world.', 'https://cdn.example.com/podcasts/crimelens_thumb.jpg', 'https://cdn.example.com/podcasts/crimelens_image.jpg', 'some-producer-id');

-- GENRE–PODCAST RELATIONS
INSERT INTO genre_podcast (genre_id, podcast_id) VALUES
('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
('33333333-3333-3333-3333-333333333333', 'cccccccc-cccc-cccc-cccc-cccccccccccc');

-- EPISODES
INSERT INTO episode (episode_id, url, title, description, release_date, duration_seconds, podcast_id, thumbnail_url, image_url) VALUES
('aaaa1111-1111-1111-1111-aaaaaaaa1111', 'https://cdn.edufy.se/podcasts/codetalk/ep1.mp3', 'The Rise of Microservices', 'Discussion about microservice patterns and pitfalls.', '2024-09-01', 1800, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'https://cdn.example.com/episodes/codetalk_ep1_thumb.jpg', 'https://cdn.example.com/episodes/codetalk_ep1_image.jpg'),
('aaaa2222-2222-2222-2222-aaaaaaaa2222', 'https://cdn.edufy.se/podcasts/codetalk/ep2.mp3', 'Unit Testing Strategies', 'How to build better test coverage.', '2024-09-08', 2100, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'https://cdn.example.com/episodes/codetalk_ep2_thumb.jpg', 'https://cdn.example.com/episodes/codetalk_ep2_image.jpg'),
('bbbb1111-1111-1111-1111-bbbbbbbb1111', 'https://cdn.edufy.se/podcasts/laughtrack/ep1.mp3', 'Bananas in Space', 'Improvised sci-fi sketch.', '2024-08-12', 2400, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'https://cdn.example.com/episodes/laughtrack_ep1_thumb.jpg', 'https://cdn.example.com/episodes/laughtrack_ep1_image.jpg'),
('bbbb2222-2222-2222-2222-bbbbbbbb2222', 'https://cdn.edufy.se/podcasts/laughtrack/ep2.mp3', 'The Lost Microphone', 'Comedy about podcasting fails.', '2024-08-19', 2500, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'https://cdn.example.com/episodes/laughtrack_ep2_thumb.jpg', 'https://cdn.example.com/episodes/laughtrack_ep2_image.jpg'),
('cccc1111-1111-1111-1111-cccccccc1111', 'https://cdn.edufy.se/podcasts/crimelens/ep1.mp3', 'The Shadow of Truth', 'Deep dive into a 90s cold case.', '2024-10-01', 2700, 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'https://cdn.example.com/episodes/crimelens_ep1_thumb.jpg', 'https://cdn.example.com/episodes/crimelens_ep1_image.jpg'),
('cccc2222-2222-2222-2222-cccccccc2222', 'https://cdn.edufy.se/podcasts/crimelens/ep2.mp3', 'Forensics Gone Wrong', 'How false evidence changed a case.', '2024-10-08', 2900, 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'https://cdn.example.com/episodes/crimelens_ep2_thumb.jpg', 'https://cdn.example.com/episodes/crimelens_ep2_image.jpg');
