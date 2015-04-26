drop view if exists FeatureWithReasonView;

create view FeatureWithReasonView as (
	select Feature.id as id,
		Feature.nullCheckId as nullCheckId,
		Feature.extractor as extractor,
		Feature.manifestation as manifestation,
		Reason.id as reasonId,
		NodeReason.nodeId as nodeId,
		FeatureReason.reasonFeatureId as reasonFeatureId
	from Feature
		left outer join Reason on (Reason.featureId = Feature.id)
		left outer join NodeReason on (NodeReason.id = Reason.id)
		left outer join FeatureReason on (FeatureReason.id = Reason.id)
);
