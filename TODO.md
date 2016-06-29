# TO DO

## Cucumber Reports

- Publish the cucumber json reports to indicate what is passing, failing and skipped
- Should be done for every commit and pull request - for developers
- Should also be done for every release - for library users
- Ideally, if a scenario has an issue tag, then
    - if it's passing in the release, then the issue should be marked as done
    - if it's passing in a commit, but not the release, then it should be labelled "pending release"
    - if it's passing in a pull-request, but not a commit, then it should be labelled "pending merge"
- For each of the json reports, it would be nice to produce a feature coverage report. i.e. for all the defined
  scenarios, what % has been implemented
- So that we can see the rate of feature delivery, it would also be nice to show:
    - a burn down of feature coverage
    - a cumulative flow chart of features
    - a chart of feature cycle time
- It might also make sense to take the list of committed features and the list of passing features in the release to
  produce a feature coverage report for documented features vs what's actually been released