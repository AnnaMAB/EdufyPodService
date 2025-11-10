# -- GENRES
# INSERT INTO genre (genre_id, name) VALUES
# ('11111111-1111-1111-1111-111111111111', 'Tech'),
# ('22222222-2222-2222-2222-222222222222', 'Comedy'),
# ('33333333-3333-3333-3333-333333333333', 'True Crime');
#
# -- PODCASTS
# INSERT INTO podcast (podcast_id, name, description) VALUES
# ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'CodeTalk', 'A podcast about software development, architecture, and frameworks.'),
# ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'LaughTrack', 'Comedy podcast with improv and absurd humor.'),
# ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'CrimeLens', 'Investigating real-world crime stories from around the world.');
#
# -- GENRE–PODCAST RELATIONS
# INSERT INTO genre_podcast (genre_id, podcast_id) VALUES
# ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
# ('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
# ('33333333-3333-3333-3333-333333333333', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
# ('11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb');
#
# -- EPISODES
# INSERT INTO episode (episode_id, url, title, description, release_date, duration_seconds, podcast_id) VALUES
# ('aaaa1111-1111-1111-1111-aaaaaaaa1111', 'https://cdn.edufy.se/podcasts/codetalk/ep1.mp3', 'The Rise of Microservices', 'Discussion about microservice patterns and pitfalls.', '2024-09-01', 1800, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
# ('aaaa2222-2222-2222-2222-aaaaaaaa2222', 'https://cdn.edufy.se/podcasts/codetalk/ep2.mp3', 'Unit Testing Strategies', 'How to build better test coverage.', '2024-09-08', 2100, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
# ('bbbb1111-1111-1111-1111-bbbbbbbb1111', 'https://cdn.edufy.se/podcasts/laughtrack/ep1.mp3', 'Bananas in Space', 'Improvised sci-fi sketch.', '2024-08-12', 2400, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
# ('bbbb2222-2222-2222-2222-bbbbbbbb2222', 'https://cdn.edufy.se/podcasts/laughtrack/ep2.mp3', 'The Lost Microphone', 'Comedy about podcasting fails.', '2024-08-19', 2500, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
# ('cccc1111-1111-1111-1111-cccccccc1111', 'https://cdn.edufy.se/podcasts/crimelens/ep1.mp3', 'The Shadow of Truth', 'Deep dive into a 90s cold case.', '2024-10-01', 2700, 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
# ('cccc2222-2222-2222-2222-cccccccc2222', 'https://cdn.edufy.se/podcasts/crimelens/ep2.mp3', 'Forensics Gone Wrong', 'How false evidence changed a case.', '2024-10-08', 2900, 'cccccccc-cccc-cccc-cccc-cccccccccccc');
